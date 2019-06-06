QT += testlib sql positioning
QT -= gui

CONFIG += qt console warn_on depend_includepath testcase
CONFIG -= app_bundle

TEMPLATE = app

SOURCES +=  tst_municipalityservicetest.cpp

win32:CONFIG(release, debug|release): LIBS += -L$$OUT_PWD/../iDispatchGisData/release/ -liDispatchGisData
else:win32:CONFIG(debug, debug|release): LIBS += -L$$OUT_PWD/../iDispatchGisData/debug/ -liDispatchGisData
else:unix: LIBS += -L$$OUT_PWD/../iDispatchGisData/ -liDispatchGisData

INCLUDEPATH += $$PWD/../iDispatchGisData
DEPENDPATH += $$PWD/../iDispatchGisData
