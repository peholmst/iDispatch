// iDispatch Station Alert
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

#include <boost/format.hpp>
#include <cairomm/context.h>
#include <glibmm/main.h>

#include "DateWidget.hpp"

const std::string WEEKDAYS_SV[] = {"söndag", "måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag"};
const std::string WEEKDAYS_FI[] = {"sunnuntai", "maanantai", "tiistai", "keskiviikko", "torstai", "perjantai", "lauantai"};
const std::string MONTHS_SV[] = {"januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "december"};
const std::string MONTHS_FI[] = {"tammikuuta", "helmikuuta", "maaliskuuta", "huhtikuuta", "toukokuuta", "kesäkuuta", "heinäkuuta", "elokuuta", "syyskuuta", "lokakuuta", "marraskuuta", "joulukuuta"};
const std::string DATE_FORMAT_SV = "%1% %2% %3%";
const std::string DATE_FORMAT_FI = "%1%. %2% %3%";

ui::DateWidget::DateWidget() : locale(utils::FINNISH)
{
    signal_timeout_connection = Glib::signal_timeout().connect(sigc::mem_fun(*this, &DateWidget::on_timeout), 1000);
}

ui::DateWidget::~DateWidget()
{
    signal_timeout_connection.disconnect();
}

void ui::DateWidget::changeLocale(const utils::Locale locale)
{
    this->locale = locale;
    this->on_timeout();
}

bool ui::DateWidget::on_draw(const Cairo::RefPtr<Cairo::Context> &cr)
{
    Gtk::Allocation allocation = get_allocation();
    const int width = allocation.get_width();
    const int height = allocation.get_height();

    cr->translate(width / 2., height / 2.);
    cr->scale(width, height);

    // Background
    cr->set_source_rgba(0.0, 0, 0, 0.0); // transparent
    cr->paint();

    cr->set_source_rgba(1.0, 1.0, 1.0, 0.8); // TODO Get color from stylesheet

    // Current date
    auto now = std::chrono::system_clock::now();
    auto tt = std::chrono::system_clock::to_time_t(now);
    auto lt = *localtime(&tt);

    Cairo::TextExtents extents;

    // TODO Adjust font size so that the date string always fits into the box
    cr->set_font_size(0.08);

    // Weekday
    std::string weekday = (locale == utils::FINNISH ? WEEKDAYS_FI[lt.tm_wday] : WEEKDAYS_SV[lt.tm_wday]);
    cr->get_text_extents(weekday, extents);
    cr->move_to(extents.width / -2, (extents.height / -2) * 1.15);
    cr->show_text(weekday);

    // Date
    std::string dateString = boost::str(boost::format((locale == utils::FINNISH ? DATE_FORMAT_FI : DATE_FORMAT_SV)) % lt.tm_mday % (locale == utils::FINNISH ? MONTHS_FI[lt.tm_mon] : MONTHS_SV[lt.tm_mon]) % (lt.tm_year + 1900));
    cr->get_text_extents(dateString, extents);
    cr->move_to(extents.width / -2, (extents.height / 2) * 1.15);
    cr->show_text(dateString);

    return true;
}

bool ui::DateWidget::on_timeout()
{
    auto win = get_window();
    if (win)
    {
        Gdk::Rectangle r(0, 0, get_allocation().get_width(), get_allocation().get_height());
        win->invalidate_rect(r, false);
    }
    return true;
}