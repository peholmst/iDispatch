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

#include <iostream>
#include <sstream>

#include <gtkmm/application.h>
#include <gtkmm/cssprovider.h>

#include "MainWindow.hpp"

extern char _binary_res_styles_css_start;
extern char _binary_res_styles_css_end;

int main(int argc, char *argv[])
{
    // Load CSS into a std::string
    std::stringstream css;
    char *p = &_binary_res_styles_css_start;
    while (p != &_binary_res_styles_css_end)
        css.put(*p++);

    // Create application and main window
    auto app = Gtk::Application::create(argc, argv, "net.pkhapps.idispatch.stationalert");
    ui::MainWindow mainWindow;

    // Load CSS
    auto cssProvider = Gtk::CssProvider::create();
    cssProvider->load_from_data(css.str());
    auto styleContext = Gtk::StyleContext::create();
    auto screen = Gdk::Screen::get_default();
    styleContext->add_provider_for_screen(screen, cssProvider, GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);

    // Run app
    return app->run(mainWindow);
}