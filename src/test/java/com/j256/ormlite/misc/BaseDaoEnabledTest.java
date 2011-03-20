package com.j256.ormlite.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.j256.ormlite.BaseCoreTest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

public class BaseDaoEnabledTest extends BaseCoreTest {

	@Test
	public void testBaseDaoEnabled() throws Exception {
		Dao<One, Integer> dao = createDao(One.class, true);
		One one = new One();
		String stuff = "fewpfjewfew";
		one.stuff = stuff;
		one.setDao(dao);
		assertEquals(1, one.create());
	}

	@Test
	public void testForeign() throws Exception {
		Dao<One, Integer> oneDao = createDao(One.class, true);
		Dao<ForeignDaoEnabled, Integer> foreignDao = createDao(ForeignDaoEnabled.class, true);

		One one = new One();
		String stuff = "fewpfjewfew";
		one.stuff = stuff;
		one.setDao(oneDao);
		assertEquals(1, one.create());

		ForeignDaoEnabled foreign = new ForeignDaoEnabled();
		foreign.one = one;
		foreign.setDao(foreignDao);
		assertEquals(1, foreign.create());

		ForeignDaoEnabled foreign2 = foreignDao.queryForId(foreign.id);
		assertNotNull(foreign2);
		assertEquals(one.id, foreign2.one.id);
		assertNull(foreign2.one.stuff);
		assertEquals(1, foreign2.one.refresh());
		assertEquals(stuff, foreign2.one.stuff);
	}

	/* ============================================================================================== */

	protected static class One extends BaseDaoEnabled<One, Integer> {
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField
		public String stuff;
		public One() {
		}
	}

	protected static class ForeignDaoEnabled extends BaseDaoEnabled<ForeignDaoEnabled, Integer> {
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField(foreign = true)
		public One one;
		public ForeignDaoEnabled() {
		}
	}
}