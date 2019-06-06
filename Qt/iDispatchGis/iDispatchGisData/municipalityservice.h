#ifndef MUNICIPALITYSERVICE_H
#define MUNICIPALITYSERVICE_H

#include <stdexcept>

#include <QObject>
#include <QSqlDatabase>
#include <QSqlDriver>
#include <QSqlError>
#include <QSqlQuery>

#include "gisdata.h"
#include "page.h"

class NoSuchMunicipalityException : public std::runtime_error
{
public:
    explicit NoSuchMunicipalityException(const std::string& what_arg);
};

class DatabaseException : public std::runtime_error
{
public:
    explicit DatabaseException(const std::string& what_arg);
};

class MunicipalityService : public QObject
{
    Q_OBJECT
public:
    explicit MunicipalityService(QSqlDatabase& db, QObject* parent = nullptr);

    Page<Municipality> findAll(const PageRequest& pageRequest);
    Page<Municipality> findByName(const QString& name, const PageRequest& pageRequest);
    Municipality findByCode(const MunicipalityCode& code);

    bool contains(const MunicipalityCode& code);

    void add(const Municipality& municipality);
    void remove(const MunicipalityCode& code);

signals:

public slots:

private:
    QSqlDatabase m_db;

    bool isDatabaseInitialized();
    void initializeDatabase();
    void execDDL(const QString& ddl);
    Page<Municipality> createPage(QSqlQuery& countQuery, QSqlQuery& pageQuery, const PageRequest& pageRequest);
};

#endif // MUNICIPALITYSERVICE_H
