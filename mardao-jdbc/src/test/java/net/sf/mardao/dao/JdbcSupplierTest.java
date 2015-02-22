package net.sf.mardao.dao;

/*
 * #%L
 * mardao-gae
 * %%
 * Copyright (C) 2010 - 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import net.sf.mardao.domain.DUser;
import net.sf.mardao.domain.DFactory;
import net.sf.mardao.testing.dao.InMemoryDataFieldMaxValueIncrementer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tests for JdbcSupplier.
 *
 * @author osandstrom Date: 2014-09-12 Time: 20:17
 */
public class JdbcSupplierTest extends AbstractDaoTest {

  final DataSource dataSource = new SingleConnectionDataSource(
          "jdbc:h2:mem:typeDaoTest", "mardao", "jUnit", true);
  final InMemoryDataFieldMaxValueIncrementer incrementer =
          new InMemoryDataFieldMaxValueIncrementer();

  @Before
  @Override
  public void setUp() {
    supplier = new JdbcSupplier(dataSource, incrementer);
    userDao = new DUserDao(supplier);
    factoryDao = new DFactoryDao(supplier);
    createDatabaseTables(dataSource);
  }

  public static void createDatabaseTables(DataSource dataSource) {
    final JdbcTemplate template = new JdbcTemplate(dataSource);
    String sql = "CREATE TABLE DUser (id BIGINT PRIMARY KEY, displayName VARCHAR(500), email VARCHAR(500), birthDate TIMESTAMP, createdBy VARCHAR(500))";
    template.execute(sql);
    sql = "CREATE TABLE DFactory (providerId VARCHAR(255) PRIMARY KEY)";
    template.execute(sql);
  }

  @Override
  protected void createQueryFixtures() throws IOException {
    AbstractDao.setPrincipalName(PRINCIPAL_FIXTURE);
    for (int i = 1; i < 60; i++) {
      DUser u = new DUser();
//      u.setId(Long.valueOf(i));
      u.setDisplayName("mod7_" + (i % 7));
      u.setEmail("user_" + i + "@example.com");
      userDao.put(u);

      u = new DUser();
//      u.setId(Long.valueOf(1000 + i));
      u.setDisplayName("user_" + i);
      u.setEmail("user_1000_" + i + "@example.com");
      userDao.put(u);
    }

    DFactory f = new DFactory();
    f.setProviderId("facebook");
    factoryDao.put(f);
    AbstractDao.setPrincipalName(PRINCIPAL_SET_UP);
  }

  @Override
  @Test
  public void testCount() throws IOException {
    createQueryFixtures();
    try {
      Thread.sleep(150L);
    } catch (InterruptedException e) {
      throw new IOException("sleeping", e);
    }
    int count = userDao.count();
    assertTrue(Integer.toString(count), 114 <= count);
    assertEquals(1, factoryDao.count());
  }

  @Override
  @Test
  public void testQueryByField() throws IOException {
    createQueryFixtures();

    Iterable<DUser> users = userDao.queryByDisplayName("mod7_2");
    int count = 0;
    for (DUser u : users) {
      count++;
      assertEquals("mod7_2", u.getDisplayName());
      assertEquals(2, u.getId() % 7);
    }
    assertTrue(Integer.toString(count), 8 <= count);

    users = userDao.queryByDisplayName(null);
    assertFalse(users.iterator().hasNext());
  }

  @After
  public void tearDown() {

  }
}
