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

#include "MainWindow.hpp"

ui::MainWindow::MainWindow()
{
    set_title("iDispatch Station Alert");
    set_hide_titlebar_when_maximized(true);

    maximize();

    clockBox.pack_start(clock);
    clockBox.pack_start(date);
    add(clockBox);

    showClock();
}

ui::MainWindow::~MainWindow()
{
}

void ui::MainWindow::showAlert(const alert::Alert alert)
{
}

void ui::MainWindow::hideAlert(const alert::AlertId alertId)
{
}

void ui::MainWindow::showConnectionError()
{
}

void ui::MainWindow::hideConnectionError()
{
}

void ui::MainWindow::showClock()
{
    clockBox.show_all();
}

void ui::MainWindow::hideClock()
{
}
