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

#ifndef __MAIN_WINDOW_HPP__
#define __MAIN_WINDOW_HPP_

#include <gtkmm/box.h>
#include <gtkmm/window.h>

#include "Alert.hpp"
#include "ClockWidget.hpp"
#include "DateWidget.hpp"

namespace ui
{
    class MainWindow : public Gtk::Window
    {
    public:
        MainWindow();
        virtual ~MainWindow();
        void showAlert(const alert::Alert alert);
        void hideAlert(const alert::AlertId alertId);
        void showConnectionError();
        void hideConnectionError();
        void showClock();
        void hideClock();

    private:
        Gtk::Box clockBox;
        ui::ClockWidget clock;
        ui::DateWidget date;
    };
};

#endif