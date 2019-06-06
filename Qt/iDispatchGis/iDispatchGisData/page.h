#ifndef PAGE_H
#define PAGE_H

#include <stdexcept>

#include <QVector>

#include "idispatchgisdata_global.h"

class IDISPATCHGISDATASHARED_EXPORT PageRequest
{
public:
    static PageRequest of(const qint32 page, const qint32 size)
    {
        return PageRequest(page, size);
    }

    qint32 offset() const
    {
        return m_offset;
    }

    qint32 pageNumber() const
    {
        return m_pageNumber;
    }

    qint32 pageSize() const
    {
        return m_pageSize;
    }

    qint32 totalCount() const
    {
        return m_totalCount;
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
        return m_offset + m_pageSize < m_totalCount;
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

    PageRequest withTotalCount(const qint32 totalCount) const
    {
        return PageRequest(m_pageNumber, m_pageSize, totalCount);
    }
protected:
    explicit PageRequest(const qint32 pageNumber, const qint32 pageSize, const qint32 totalCount):
        m_offset(pageSize * pageNumber), m_pageNumber(pageNumber), m_pageSize(pageSize), m_totalCount(totalCount)
    {
    }

private:
    explicit PageRequest(const qint32 pageNumber, const qint32 pageSize):
        m_offset(pageSize * pageNumber), m_pageNumber(pageNumber), m_pageSize(pageSize), m_totalCount(-1)
    {
    }

    const qint32 m_offset;
    const qint32 m_pageNumber;
    const qint32 m_pageSize;
    const qint32 m_totalCount;
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
