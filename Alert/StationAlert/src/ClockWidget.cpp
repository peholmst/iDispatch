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

#include <chrono>
#include <ctime>
#include <cmath>
#include <cairomm/context.h>
#include <glibmm/main.h>

#include "ClockWidget.hpp"

ui::ClockWidget::ClockWidget() : radius(0.42), lineWidth(0.05)
{
    signal_timeout_connection = Glib::signal_timeout().connect(sigc::mem_fun(*this, &ClockWidget::on_timeout), 1000);
}

ui::ClockWidget::~ClockWidget()
{
    signal_timeout_connection.disconnect();
}

// Implementation based on https://developer-old.gnome.org/gtkmm-tutorial/3.24/sec-drawing-clock-example.html.en
bool ui::ClockWidget::on_draw(const Cairo::RefPtr<Cairo::Context> &cr)
{
    Gtk::Allocation allocation = get_allocation();
    const int width = allocation.get_width();
    const int height = allocation.get_height();

    if (width < height)
    {
        cr->translate(width / 2., width / 2.);
        cr->scale(width, width);
    }
    else
    {
        cr->translate(height / 2., height / 2.);
        cr->scale(height, height);
    }

    cr->set_line_width(lineWidth);

    // Background
    cr->save();
    cr->set_source_rgba(0, 0, 0, 0); // transparent
    cr->paint();
    cr->restore();

    // Clock face
    cr->arc(0, 0, radius, 0, 2 * M_PI);
    cr->save();
    cr->set_source_rgba(1.0, 1.0, 1.0, 0.8);
    cr->fill_preserve();
    cr->restore();
    cr->stroke_preserve();
    cr->clip();

    // Clock ticks
    for (int i = 0; i < 12; ++i)
    {
        double inset = 0.05;
        cr->save();
        cr->set_line_cap(Cairo::LINE_CAP_ROUND);

        if (i % 3 != 0)
        {
            inset *= 0.8;
            cr->set_line_width(0.03);
        }

        cr->move_to(
            (radius - inset) * cos(i * M_PI / 6),
            (radius - inset) * sin(i * M_PI / 6));
        cr->line_to(
            radius * cos(i * M_PI / 6),
            radius * sin(i * M_PI / 6));
        cr->stroke();
        cr->restore();
    }

    // Current time
    auto now = std::chrono::system_clock::now();
    auto tt = std::chrono::system_clock::to_time_t(now);
    auto lt = *localtime(&tt);

    double minutes = lt.tm_min * M_PI / 30;
    double hours = lt.tm_hour * M_PI / 6;
    double seconds = lt.tm_sec * M_PI / 30;

    cr->save();
    cr->set_line_cap(Cairo::LINE_CAP_ROUND);

    // Seconds hand
    cr->save();
    cr->set_line_width(lineWidth / 3);
    cr->set_source_rgba(0.7, 0.7, 0.7, 0.8); // TODO Get color from stylesheet
    cr->move_to(0, 0);
    cr->line_to(sin(seconds) * (radius * 0.9), -cos(seconds) * (radius * 0.9));
    cr->stroke();
    cr->restore();

    // Minutes hand
    cr->set_source_rgba(0.117, 0.337, 0.612, 0.9); // TODO Get color from stylesheet
    cr->move_to(0, 0);
    cr->line_to(sin(minutes + seconds / 60) * (radius * 0.8), -cos(minutes + seconds / 60) * (radius * 0.8));
    cr->stroke();

    // Hours hand
    cr->set_source_rgba(0.337, 0.612, 0.117, 0.9); // TODO Get color from stylesheet
    cr->move_to(0, 0);
    cr->line_to(sin(hours + minutes / 12.0) * (radius * 0.5), -cos(hours + minutes / 12.0) * (radius * 0.5));
    cr->stroke();
    cr->restore();

    // Dot in the middle
    cr->arc(0, 0, lineWidth / 3.0, 0, 2 * M_PI);
    cr->fill();

    return true;
}

bool ui::ClockWidget::on_timeout()
{
    auto win = get_window();
    if (win)
    {
        Gdk::Rectangle r(0, 0, get_allocation().get_width(), get_allocation().get_height());
        win->invalidate_rect(r, false);
    }
    return true;
}