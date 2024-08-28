package controller;

public class GenericController {
	
	public static String replacePortugueseSpecialCharacters(String input) {
        if (input == null)
            return null;
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }

}
