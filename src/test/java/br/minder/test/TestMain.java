package br.minder.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import java.util.TimeZone;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import br.minder.MinderApplication;

@Rollback
@ActiveProfiles("application-test")
public class TestMain {

	@Test
	public void testMain() throws Exception {
		String[] args = {};
		System.setProperty("spring.profiles.active", "test");
		MinderApplication.main(args);
		assertThat(TimeZone.getDefault().getID(), equalTo("GMT"));
	}

}