#include <QtTest>

#include "municipalityservice.h"

class MunicipalityServiceTest : public QObject
{
    Q_OBJECT

public:
    MunicipalityServiceTest();
    ~MunicipalityServiceTest();

private slots:
    void test_addAndFindByCode();
    void test_addAndRemove();
    void test_findByName();

private:
    QSqlDatabase m_db;
};

MunicipalityServiceTest::MunicipalityServiceTest()
    : m_db(QSqlDatabase::addDatabase("QSQLITE"))
{
    m_db.setDatabaseName(":memory:");
    m_db.open();
}

MunicipalityServiceTest::~MunicipalityServiceTest()
{
    m_db.close();
}

void MunicipalityServiceTest::test_addAndFindByCode()
{
    MunicipalityService service(m_db, this);
    Municipality toAdd = { "123", "Kommun", "Kunta" };
    service.add(toAdd);
    Municipality retrieved = service.findByCode("123");
    QCOMPARE(retrieved, toAdd);
}

void MunicipalityServiceTest::test_addAndRemove()
{
    MunicipalityService service(m_db, this);
    Municipality toAdd = { "456", "Kommun2", "Kunta2" };
    service.add(toAdd);
    QVERIFY(service.contains("456"));
    service.remove("456");
    QVERIFY(!service.contains("456"));
}

void MunicipalityServiceTest::test_findByName()
{
    MunicipalityService service(m_db, this);
    for (int i = 0; i < 25; ++i)
    {
        MunicipalityCode code = QString::number(i);
        service.add(Municipality{code, "Swe" + code, "Fin" + code});
    }

    Page<Municipality> firstPage = service.findByName("fin", PageRequest::of(0, 10));
    QVERIFY(firstPage.content().size() == 10);
    QVERIFY(firstPage.pageRequest().totalCount() == 25);
    QVERIFY(firstPage.pageRequest().offset() == 0);
    QVERIFY(firstPage.pageRequest().isPaged());
    QVERIFY(firstPage.pageRequest().hasNext());

    Page<Municipality> secondPage = service.findByName("fin", firstPage.pageRequest().next());
    QVERIFY(secondPage.content().size() == 10);
    QVERIFY(secondPage.pageRequest().totalCount() == 25);
    QVERIFY(secondPage.pageRequest().offset() == 10);
    QVERIFY(secondPage.pageRequest().isPaged());
    QVERIFY(secondPage.pageRequest().hasNext());

    Page<Municipality> thirdPage = service.findByName("fin", secondPage.pageRequest().next());
    QVERIFY(thirdPage.content().size() == 5);
    QVERIFY(thirdPage.pageRequest().totalCount() == 25);
    QVERIFY(thirdPage.pageRequest().offset() == 20);
    QVERIFY(thirdPage.pageRequest().isPaged());
    QVERIFY(!thirdPage.pageRequest().hasNext());
}

QTEST_APPLESS_MAIN(MunicipalityServiceTest)

#include "tst_municipalityservicetest.moc"
