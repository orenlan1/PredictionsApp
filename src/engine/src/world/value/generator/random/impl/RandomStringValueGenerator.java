package world.value.generator.random.impl;

import world.value.generator.random.api.AbstractRandomValueGenerator;

public class RandomStringValueGenerator extends AbstractRandomValueGenerator<String> {
    private final int minLength = 1;
    private final int maxLength = 50;
    private final String VALID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?,-.() ";

    @Override
    public String generateValue() {

        int length = minLength + random.nextInt(maxLength - minLength + 1);

        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(VALID_CHARS.length());
            char randomChar = VALID_CHARS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}
