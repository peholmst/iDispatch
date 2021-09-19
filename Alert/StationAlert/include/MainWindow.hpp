#ifndef __MAIN_WINDOW_HPP__
#define __MAIN_WINDOW_HPP_

#include <gtkmm/window.h>

class MainWindow : public Gtk::Window
{
public:
    MainWindow();
    virtual ~MainWindow();
};

#endif