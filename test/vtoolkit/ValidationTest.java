package vtoolkit;

import toolkit.ValidationToolkit;

public class ValidationTest {

	public static void main(String[] args) {
		String input = "hello@";
		
		System.out.println(ValidationToolkit.validateLettersOnly(input));
	}
}
