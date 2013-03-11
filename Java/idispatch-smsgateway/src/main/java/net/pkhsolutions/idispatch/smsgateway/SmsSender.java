package net.pkhsolutions.idispatch.smsgateway;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pkhsolutions.idispatch.rest.client.DispatcherClientException;
import net.pkhsolutions.idispatch.rest.client.Notification;
import net.pkhsolutions.idispatch.rest.client.Notifications;
import net.pkhsolutions.idispatch.rest.client.ServerPoller;
import org.smslib.GatewayException;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.helper.CommPortIdentifier;
import org.smslib.modem.SerialModemGateway;

public class SmsSender implements ServerPoller.Callback {

    private final Configuration configuration;
    private final SerialModemGateway gateway;
    private static final Logger LOG = Logger.getLogger(SmsSender.class.getName());
    private boolean errorReceived = false;

    public SmsSender(Configuration configuration) throws GatewayException {
        this.configuration = configuration;
        gateway = new SerialModemGateway("modem", configuration.getPort(), configuration.getBaudrate(), "Huawei", "");
        gateway.setOutbound(true);
        gateway.setInbound(false);

        Service.getInstance().addGateway(gateway);
    }

    public void connectToModem() throws IOException, TimeoutException, GatewayException, InterruptedException, SMSLibException {
        LOG.log(Level.INFO, "Connecting to modem");
        Service.getInstance().startService();
    }

    public void disconnectFromModem() throws IOException, SMSLibException, TimeoutException, GatewayException, InterruptedException {
        LOG.log(Level.INFO, "Disconnecting from modem");
        Service.getInstance().stopService();
    }

    @Override
    public void clearErrorCode() {
        if (errorReceived) {
            LOG.info("Operating normally again");
            errorReceived = false;
        }
    }

    @Override
    public void setErrorCode(DispatcherClientException.ErrorCode errorCode) {
        LOG.log(Level.SEVERE, "Received error code {0}, will not receive any dispatch notifications", errorCode);
        errorReceived = true;
    }

    @Override
    public void notificationsReceived(Notifications notifications) {
        for (Notification n : notifications.getNotifications()) {
            try {
                sendSmsForNotification(n);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Could not send out SMS", e);
            }
        }
    }

    private void sendSmsForNotification(Notification notification) throws Exception {
        Set<String> destinations = new HashSet<>();

        StringBuilder sb = new StringBuilder();
        sb.append(notification.getTicketTypeCode());
        sb.append(notification.getUrgency().toString());
        sb.append(": ");
        sb.append(notification.getMunicipalitySv());
        sb.append(", ");
        sb.append(notification.getAddress());
        sb.append("; ");
        for (Iterator<String> it = notification.getTicketResources().iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("; ");
        sb.append(notification.getDescription());
        String msg = sb.toString();
        if (msg.length() > 160) {
            msg = msg.substring(0, 160);
        }
        for (String dispatchedResource : notification.getResources()) {
            destinations.addAll(configuration.getNumbersForResource(dispatchedResource));
        }

        for (String destination : destinations) {
            sendSms(destination, msg);
        }
    }

    private void sendSms(String destination, String message) throws TimeoutException, GatewayException, IOException, InterruptedException {
        LOG.log(Level.INFO, "Sending message \"{0}\" to {1}", new Object[]{message, destination});
        if (Service.getInstance().sendMessage(new OutboundMessage(destination, message))) {
            LOG.log(Level.INFO, "Message sent successfully");
        } else {
            LOG.log(Level.WARNING, "Message was not sent");
        }
    }

    public static void main(String[] argas) {
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        while (portIdentifiers.hasMoreElements()) {
            System.out.println(portIdentifiers.nextElement().getName());
        }
    }
}
