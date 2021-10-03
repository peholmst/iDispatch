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

#ifndef __DATE_WIDGET_HPP__
#define __DATE_WIDGET_HPP__

#include <gtkmm/drawingarea.h>

#include "Locale.hpp"

namespace ui
{
    class DateWidget : public Gtk::DrawingArea
    {
    public:
        DateWidget();
        virtual ~DateWidget();
        void changeLocale(const utils::Locale locale);

    protected:
        bool on_draw(const Cairo::RefPtr<Cairo::Context> &cr) override;
        bool on_timeout();

    private:
        sigc::connection signal_timeout_connection;
        utils::Locale locale;
    };
};

#endif