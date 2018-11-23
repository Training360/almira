package training360.epbva;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpBvaApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testValidateEdgeA(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.validate(200, 150, 150).toString();
		//Then
		assertThat(message, equalTo("[A háromszög \"a\" oldala nagyobb, mint 200!]"));
	}

	@Test
	public void testValidateEdgeB(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.validate(150, 200, 150).toString();
		//Then
		assertThat(message, equalTo("[A háromszög \"b\" oldala nagyobb, mint 200!]"));
	}

	@Test
	public void testValidateEdgeC(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.validate(150, 150, 200).toString();
		//Then
		assertThat(message, equalTo("[A háromszög \"c\" oldala nagyobb, mint 200!]"));
	}

	@Test
	public void testClassifyAsIsosceles(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.classify(10, 10, 20).toString();
		//Then
		assertThat(message, equalTo("ISOSCELES"));
	}

	@Test
	public void testClassifyAsScaleneA(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.classify(20, 15, 5).toString();
		//Then
		assertThat(message, equalTo("SCALENE"));
	}

	@Test
	public void testClassifyAsScaleneB(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.classify(15, 20, 5).toString();
		//Then
		assertThat(message, equalTo("SCALENE"));
	}

	@Test
	public void testClassifyAsScaleneC(){
		//Given
		TriangleDecision td = new TriangleDecision();
		//When
		String message = td.classify(5, 15, 20).toString();
		//Then
		assertThat(message, equalTo("SCALENE"));
	}
}
