#ifndef PAGE_H
#define PAGE_H

#include <stdexcept>

#include <QVector>

#include "idispatchgisdata_global.h"

class IDISPATCHGISDATASHARED_EXPORT PageRequest
{
public:
    static PageRequest of(const int page, const int size)
    {
        return PageRequest(page, size);
    }

    long offset() const
    {
        return m_offset;
    }

    int pageNumber() const
    {
        return m_pageNumber;
    }

    int pageSize() const
    {
        return m_pageSize;
    }

    bool isPaged() const
    {
        return m_totalCount != -1;
    }

    bool isUnpaged() const
    {
        return m_totalCount == -1;
    }

    bool hasNext() const
    {
        return m_offset + m_pageSize > m_totalCount;
    }

    PageRequest next() const
    {
        if (!hasNext())
        {
            throw std::out_of_range("No next page");
        }
        return PageRequest(m_pageNumber + 1, m_pageSize, m_totalCount);
    }

    bool hasPrevious() const
    {
        return isPaged() && (m_offset - m_pageSize >= 0);
    }

    PageRequest previous() const
    {
        if (!hasPrevious())
        {
            throw std::out_of_range("No previous page");
        }
        return PageRequest(m_pageNumber - 1, m_pageSize, m_totalCount);
    }

protected:
    PageRequest(const int pageNumber, const int pageSize, const long totalCount):
        m_offset(pageSize * pageNumber), m_pageNumber(pageNumber), m_pageSize(pageSize), m_totalCount(totalCount)
    {
    }

private:
    PageRequest(const int pageNumber, const int pageSize):
        m_offset(pageSize * pageNumber), m_pageNumber(pageNumber), m_pageSize(pageSize), m_totalCount(-1)
    {
    }

    const long m_offset;
    const int m_pageNumber;
    const int m_pageSize;
    const long m_totalCount;
};

template <typename T>
class IDISPATCHGISDATASHARED_EXPORT Page
{
public:
    static Page<T> empty(const PageRequest pageRequest)
    {
        return Page(QVector<T>(0), pageRequest);
    }

    Page(const QVector<T> content, const PageRequest pageRequest):
        m_pageRequest(pageRequest), m_content(content)
    {
    }

    const PageRequest& pageRequest() const
    {
        return m_pageRequest;
    }

    const QVector<T>& content() const
    {
        return m_content;
    }

private:
    const PageRequest m_pageRequest;
    const QVector<T> m_content;
};

#endif // PAGE_H
