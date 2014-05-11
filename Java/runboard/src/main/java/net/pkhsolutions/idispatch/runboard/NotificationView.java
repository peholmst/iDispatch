package net.pkhsolutions.idispatch.runboard;

import net.pkhsolutions.idispatch.rest.client.Notification;
import org.apache.commons.lang.StringEscapeUtils;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class NotificationView extends JPanel {

    private final JScrollPane scrollPane;
    private final JEditorPane htmlViewer;
    private final HTMLEditorKit kit = new HTMLEditorKit();
    private final Notification notification;
    private final Language language;
    private final boolean lowRes;

    public NotificationView(Notification notification, Language language, boolean lowRes) {
        setLayout(new BorderLayout());
        this.lowRes = lowRes;
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
        if (lowRes) {
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
        for (Iterator<String> resource = notification.getTicketResources().iterator(); resource.hasNext(); ) {
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
}
