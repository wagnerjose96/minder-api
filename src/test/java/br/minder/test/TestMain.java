package br.minder.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import java.util.TimeZone;
import org.junit.Test;
import br.minder.MinderApplication;

public class TestMain {

	@Test
	public void test() throws Exception {
		String[] args = {};
		MinderApplication.main(args);
		assertThat(TimeZone.getDefault().getID(), equalTo("GMT"));
	}

}
