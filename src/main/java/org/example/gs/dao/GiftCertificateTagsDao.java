package org.example.gs.dao;

import javax.sql.DataSource;
import java.util.List;

public interface GiftCertificateTagsDao {
    void setDataSource(DataSource dataSource);
    List<Long> getTagIdByCertificateId(long id);
    void insert(long giftCertificateId, long tagId);
}
