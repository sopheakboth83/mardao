package net.sf.mardao.junit.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.nio.ByteBuffer;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import net.sf.mardao.junit.domain.DFactory;

/**
 * The DFactory domain-object specific mapping methods go here.
 *
 * Generated on 2015-02-28T10:34:05.595+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DFactoryMapper
  implements Mapper<DFactory, String> {

  private final Supplier supplier;

  public enum Field {
    PROVIDERID("providerId");

    private final String fieldName;

    Field(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }

  public DFactoryMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public String fromKey(Object key) {
    return supplier.toStringKey(key);
  }

  @Override
  public DFactory fromReadValue(Object value) {
    return fromReadValue(value, supplier);
  }

  @Override
  public <RV> DFactory fromReadValue(RV value, Supplier<Object, RV, ?, ?> specificSupplier) {
    DFactory entity = (DFactory) specificSupplier.createEntity(this, value);
    if (null != entity) {
      return entity;
    }

    entity = new DFactory();

    // set primary key:
    final Object key = specificSupplier.getKey(value, Field.PROVIDERID.getFieldName());
    entity.setProviderId(specificSupplier.toStringKey(key));

    // set all fields:
    return entity;
  }

  @Override
  public String getCreatedByColumnName() {
    return null;
  }

  @Override
  public String getCreatedDateColumnName() {
    return null;
  }

  @Override
  public String getPrimaryKeyColumnName() {
    return Field.PROVIDERID.getFieldName();
  }

  @Override
  public String getParentKeyColumnName() {
    return null;
  }

  @Override
  public String getUpdatedByColumnName() {
    return null;
  }

  @Override
  public String getUpdatedDateColumnName() {
    return null;
  }

  @Override
  public String getId(DFactory entity) {
    return entity != null ? entity.getProviderId() : null;
  }

  @Override
  public Object getParentKey(DFactory entity) {
    return null;
  }

  @Override
  public void setParentKey(DFactory entity, Object parentKey) {
    // this entity has no parent
  }

  @Override
  public void setPrimaryKey(Object writeValue, Object primaryKey) {
    supplier.setString(writeValue, Field.PROVIDERID.getFieldName(), supplier.toStringKey(primaryKey));
  }

  @Override
  public void updateEntityPostWrite(DFactory entity, Object key, Object value) {
    entity.setProviderId(supplier.toStringKey(key));
}

@Override
  public String getKind() {
    return DFactory.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, String id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(DFactory entity) {
    final String id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(this, parentKey, id, entity);
    // some suppliers cannot set the keys in above method
    supplier.setPrimaryKey(value, this, Field.PROVIDERID.getFieldName(), toKey(parentKey, id), entity);

    // set all fields:
    return value;
  }

  @Override
  public String getWriteSQL(Serializable id) {
    // FIXME: implement
    return null == id ? "" : "UPDATE TABLE DUser SET (displayName,email,createdBy,birthDate) VALUES (:displayName,:email,:createdBy,:birthDate) WHERE id=:id";
  }



  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends AbstractEntityBuilder<DFactory> {

    protected Builder() {
      super(new DFactory());
    }

    public Builder providerId(String providerId) {
      entity.setProviderId(providerId);
      return this;
    }

  }
}
