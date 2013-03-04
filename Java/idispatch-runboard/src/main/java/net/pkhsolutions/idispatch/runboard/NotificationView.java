package net.pkhsolutions.idispatch.runboard;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import net.pkhsolutions.idispatch.runboard.rest.Notification;
import net.pkhsolutions.idispatch.runboard.rest.Urgency;
import org.apache.commons.lang.StringEscapeUtils;

public class NotificationView extends JPanel {

    private final JScrollPane scrollPane;
    private final JEditorPane htmlViewer;
    private final HTMLEditorKit kit = new HTMLEditorKit();
    private final Notification notification;
    private final Language language;

    public NotificationView(Notification notification, Language language) {
        setLayout(new BorderLayout());
        htmlViewer = new JEditorPane();
        htmlViewer.setEditorKit(kit);
        htmlViewer.setDocument(kit.createDefaultDocument());
        htmlViewer.setEditable(false);
        scrollPane = new JScrollPane(htmlViewer);
        add(scrollPane, BorderLayout.CENTER);

        StyleSheet css = kit.getStyleSheet();
        css.addRule("body {background:white; font-family:sans-serif; margin:15px}");
        css.addRule(".ticketTypeCode {color:red; font-size:50pt; font-weight:bold}");
        css.addRule(".ticketTypeDescription {color:red; font-size:40pt}");
        css.addRule(".municipality {font-size:40pt; font-weight:bold; margin-top:15px; margin-bottom:5px}");
        css.addRule(".address {font-size:30pt; font-weight: bold; margin-bottom:15px}");
        css.addRule(".description {font-size:30pt}");
        css.addRule(".resources {font-size:30pt; margin-top:25px; margin-bottom:20px; color:blue}");
        css.addRule(".timestamp {font-size:20pt; font-style:italic}");

        this.notification = notification;
        this.language = language;

        htmlViewer.setText(generateHtml());

        // TODO Add feature for automatically scrolling if the text does not fit on the screen
    }

    private String generateHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");

        sb.append("<div class=\"ticketTypeCode\">");
        sb.append(StringEscapeUtils.escapeHtml(notification.getTicketTypeCode()));
        sb.append(notification.getUrgency());
        sb.append("</div>");

        sb.append("<div class=\"ticketTypeDescription\">");
        switch (language) {
            case FINNISH:
                sb.append(StringEscapeUtils.escapeHtml(notification.getTicketTypeDescriptionFi()));
                break;
            case SWEDISH:
                sb.append(StringEscapeUtils.escapeHtml(notification.getTicketTypeDescriptionSv()));
        }
        sb.append("</div>");

        sb.append("<div class=\"municipality\">");
        switch (language) {
            case FINNISH:
                sb.append(StringEscapeUtils.escapeHtml(notification.getMunicipalityFi()));
                break;
            case SWEDISH:
                sb.append(StringEscapeUtils.escapeHtml(notification.getMunicipalitySv()));
        }
        sb.append("</div>");

        sb.append("<div class=\"address\">");
        sb.append(StringEscapeUtils.escapeHtml(notification.getAddress()));
        sb.append("</div>");

        sb.append("<div class=\"description\">");
        sb.append(StringEscapeUtils.escapeHtml(notification.getDescription()));
        sb.append("</div>");

        sb.append("<div class=\"resources\">");
        for (Iterator<String> resource = notification.getResources().iterator(); resource.hasNext();) {
            sb.append(StringEscapeUtils.escapeHtml(resource.next()));
            if (resource.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("</div>");

        sb.append("<div class=\"timestamp\">");
        sb.append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(notification.getTimestamp().getTime()));
        sb.append("</div>");

        sb.append("</body></html>");
        return sb.toString();
    }

    public static void main(String[] args) {
        Set<String> resources = new HashSet<>();
        resources.add("RVSItä30");
        resources.add("RVSPg11");
        resources.add("RVSPg21");
        resources.add("RVSPg31");
        resources.add("EVS5211");
        resources.add("EVS5213");
        resources.add("EVS1218");
        resources.add("EFH20");
        resources.add("EVS01");
        Notification exampleNotification = new Notification(1L, Calendar.getInstance(), resources, "Pargas", "Parainen", 102L, "Skärgårdsvägen, 2 km från Nagu färjfäste på Pargas-sidan", "203", "Trafikolycka: medelstor", "Liikenneonnettomuus: keskisuuri", Urgency.A, "Frontalkrock personbil-lastbil, två personer fastklämda i personbilen, medvetslösa, lastbilschauffören ute");

        final JFrame frame = new JFrame("NotificationView Demo");
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(new NotificationView(exampleNotification, Language.FINNISH));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }
}
