#include <QDebug>

#include "municipalityservice.h"

NoSuchMunicipalityException::NoSuchMunicipalityException(const std::string& what_arg)
    : runtime_error(what_arg)
{
}

DatabaseException::DatabaseException(const std::string& what_arg)
    : runtime_error(what_arg)
{
}

MunicipalityService::MunicipalityService(QSqlDatabase& db, QObject* parent)
    : QObject(parent), m_db(db)
{
    initializeDatabase();
}

Page<Municipality> MunicipalityService::findAll(const PageRequest& pageRequest)
{
    QSqlQuery countQuery(m_db);
    countQuery.prepare("SELECT COUNT(code) FROM municipality");

    QSqlQuery query(m_db);
    query.prepare("SELECT code, name_swe, name_fin FROM municipality "
                  "ORDER BY name_fin "
                  "LIMIT ? OFFSET ?");
    query.addBindValue(pageRequest.pageSize());
    query.addBindValue(pageRequest.offset());

    return createPage(countQuery, query, pageRequest);
}

Page<Municipality> MunicipalityService::findByName(const QString& name, const PageRequest& pageRequest)
{
    if (name.isEmpty())
    {
        return findAll(pageRequest);
    }
    else
    {
        QString searchTerm = name.toLower() + "%";

        QSqlQuery countQuery(m_db);
        countQuery.prepare("SELECT COUNT(code) FROM municipality "
                           "WHERE LOWER(name_fin) LIKE ? OR LOWER(name_swe) LIKE ?");
        countQuery.addBindValue(searchTerm);
        countQuery.addBindValue(searchTerm);

        QSqlQuery query(m_db);
        query.prepare("SELECT code, name_swe, name_fin FROM municipality "
                      "WHERE LOWER(name_fin) LIKE ? OR LOWER(name_swe) LIKE ? "
                      "ORDER BY name_fin "
                      "LIMIT ? OFFSET ?");
        query.addBindValue(searchTerm);
        query.addBindValue(searchTerm);
        query.addBindValue(pageRequest.pageSize());
        query.addBindValue(pageRequest.offset());

        return createPage(countQuery, query, pageRequest);
    }
}

Municipality MunicipalityService::findByCode(const MunicipalityCode& code)
{
    QSqlQuery query(m_db);
    query.prepare("SELECT code, name_swe, name_fin FROM municipality WHERE code = ?");
    query.bindValue(0, code);
    if (!query.exec())
    {
        qWarning() << "Error looking up municipality: " << query.lastError().text();
        throw DatabaseException("Could not look up municipality");
    }

    if (query.first())
    {
        return Municipality{ query.value(0).toString(), query.value(1).toString(), query.value(2).toString() };
    }
    else
    {
        throw NoSuchMunicipalityException("Municipality not found ");
    }
}

bool MunicipalityService::contains(const MunicipalityCode& code)
{
    QSqlQuery query(m_db);
    query.prepare("SELECT COUNT(code) FROM municipality WHERE code = ?");
    query.bindValue(0, code);
    if (!query.exec())
    {
        qWarning() << "Error checking for municipality: " << query.lastError().text();
        throw DatabaseException("Could not check for municipality");
    }
    query.first();
    return query.value(0).toInt() == 1;
}

void MunicipalityService::add(const Municipality& municipality)
{
    QSqlQuery query(m_db);
    query.prepare("INSERT INTO municipality (code, name_swe, name_fin) VALUES (?, ?, ?)");
    query.bindValue(0, municipality.code);
    query.bindValue(1, municipality.nameSwe);
    query.bindValue(2, municipality.nameFin);
    if (!query.exec())
    {
        qWarning() << "Error adding municipality: " << query.lastError().text();
        throw DatabaseException("Could not add municipality");
    }
}

void MunicipalityService::remove(const MunicipalityCode& code)
{
    QSqlQuery query(m_db);
    query.prepare("DELETE FROM municipality WHERE code = ?");
    query.bindValue(0, code);
    if (!query.exec())
    {
        qWarning() << "Error removing municipality: " << query.lastError().text();
        throw DatabaseException("Could not remove municipality");
    }
}

bool MunicipalityService::isDatabaseInitialized()
{
    QSqlQuery query("SELECT COUNT(name) FROM sqlite_master WHERE type='table' AND name='municipality'", m_db);
    if (!query.isActive())
    {
        qWarning() << "Error checking if the municipality database was initialied: " << query.lastError().text();
        throw DatabaseException("Could not check if database was initialized");
    }

    query.first();
    return query.value(0).toInt() == 1;
}

void MunicipalityService::initializeDatabase()
{
    if (!isDatabaseInitialized())
    {
        qInfo() << "Initializing municipality database";
        execDDL("CREATE TABLE IF NOT EXISTS municipality ("
                        "code VARCHAR(10) NOT NULL PRIMARY KEY, "
                        "name_fin VARCHAR(200) NOT NULL, "
                        "name_swe VARCHAR(200) NOT NULL)");
        execDDL("CREATE INDEX municipality_name_fin ON municipality (name_fin)");
        execDDL("CREATE INDEX municipality_name_swe ON municipality (name_swe)");
        qInfo() << "Municipality database initialized";
    }
    else
    {
        qInfo() << "Municipality database is already initialized";
    }
}

void MunicipalityService::execDDL(const QString& ddl)
{
    qDebug() << "Executing DDL: " << ddl;
    QSqlQuery query(ddl, m_db);
    if (!query.isActive())
    {
        qWarning() << "Error executing DDL: " << query.lastError().text();
        throw DatabaseException("Could not execute DDL");
    }
}

Page<Municipality> MunicipalityService::createPage(QSqlQuery& countQuery, QSqlQuery& pageQuery, const PageRequest& pageRequest)
{
    if (!countQuery.exec())
    {
        qWarning() << "Error getting total count: " << countQuery.lastError().text();
        throw DatabaseException("Could not retrieve total count");
    }
    countQuery.first();
    qint32 totalCount = countQuery.value(0).toInt();
    countQuery.finish();

    if (!pageQuery.exec())
    {
        qWarning() << "Error getting page content: " << pageQuery.lastError().text();
        throw DatabaseException("Could not retrieve page content");
    }
    pageQuery.first();
    QVector<Municipality> content;
    while (pageQuery.isValid())
    {
        content.append(Municipality{pageQuery.value(0).toString(),
                                    pageQuery.value(1).toString(),
                                    pageQuery.value(2).toString()});
        pageQuery.next();
    }
    pageQuery.finish();
    return Page<Municipality>(content, pageRequest.withTotalCount(totalCount));
}
