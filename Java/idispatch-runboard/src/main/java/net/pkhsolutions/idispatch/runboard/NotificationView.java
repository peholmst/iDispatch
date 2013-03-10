package net.pkhsolutions.idispatch.runboard;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import net.pkhsolutions.idispatch.runboard.rest.Notification;
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
        css.addRule(String.format("body {background:white; font-family:sans-serif; margin:%dpx}",
                normalizePixel(15)));
        css.addRule(String.format(".ticketTypeCode {color:red; font-size:%dpx; font-weight:bold}",
                normalizePixel(50)));
        css.addRule(String.format(".ticketTypeDescription {color:red; font-size:%dpx}",
                normalizePixel(40)));
        css.addRule(String.format(".municipality {font-size:%dpx; font-weight:bold; margin-top:%dpx; margin-bottom:%dpx}",
                normalizePixel(40), normalizePixel(15), normalizePixel(5)));
        css.addRule(String.format(".address {font-size:%dpx; font-weight: bold; margin-bottom:%dpx}",
                normalizePixel(30), normalizePixel(15)));
        css.addRule(String.format(".description {font-size:%dpx}",
                normalizePixel(30)));
        css.addRule(String.format(".resources {font-size:%dpx; margin-top:%dpx; margin-bottom:%dpx; color:blue}",
                normalizePixel(30), normalizePixel(25), normalizePixel(20)));
        css.addRule(String.format(".timestamp {font-size:%dpx; font-style:italic}",
                normalizePixel(20)));

        this.notification = notification;
        this.language = language;

        htmlViewer.setText(generateHtml());

        // TODO Add feature for automatically scrolling if the text does not fit on the screen
    }

    private int normalizePixel(int pixel) {
        if (Configuration.isLowResolution()) {
            return (int) (pixel * 0.4);
        } else {
            return pixel;
        }
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
        for (Iterator<String> resource = notification.getTicketResources().iterator(); resource.hasNext();) {
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

    /*public static void main(String[] args) {
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
     }*/
}
