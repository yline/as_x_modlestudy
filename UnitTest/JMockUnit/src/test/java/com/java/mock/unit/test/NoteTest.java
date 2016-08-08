package com.java.mock.unit.test;

import com.java.mock.unit.Login;
import com.java.mock.unit.NoteDAO;
import com.java.mock.unit.User;

import junit.framework.TestCase;

import org.junit.Test;

public class NoteTest extends TestCase
{
	@Test
	public void testSaveNote()
	{
		Login login = new MockLoginImpl();
		NoteDAO noteDAO = new NoteDAO();

		User user = login.login("Mr.simple", "mine_pwd");
		noteDAO.save(user, "这是simple的笔记本内容");
	}
}
