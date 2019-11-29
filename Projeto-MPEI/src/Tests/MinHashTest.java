package Tests;

import java.util.LinkedList;
import java.util.Random;

class MinHashTest {
    public static void main(String[] args) {
        int numberOfTests = 50000;
        int numberOfHashes = 100;
        int numberSize;  //pool of possible random content will have a size of 10^numberSize
        for(int z=1; z<10;z++) {
            System.gc();
            numberSize = z;
            System.out.println("A iniciar o teste da minhash (com inteiros aleatórios) ");
            System.out.println("Numero de testes: " + numberOfTests);
            System.out.println("Numero de hashes: " + numberOfHashes);
            System.out.println("Numero de digitos: " + numberSize);

            LinkedList sentences = new LinkedList();
            LinkedList hashes = new LinkedList();
            for (int i = 0; i < numberOfTests; i++) {
                String s = generateString(new Random(), "0123654789", numberSize);
                sentences.add(Integer.parseInt(s));
                hashes.add(minHash(s, numberOfHashes));
            }

            int countClashes = 0;

            for (int i = 0; i < numberOfTests; i++) {
                String s = generateString(new Random(), "0123654789", numberSize);
                if (!sentences.contains(s) && hashes.contains(minHash(s, numberOfHashes))) {
                    countClashes++;
                }
                if (sentences.contains(s) && !hashes.contains(minHash(s, numberOfHashes))) {
                    System.out.println("Fatal error, please check code!!");
                }
                if ((i) % (numberOfTests / 10) == 0)
                    System.out.printf((double) i / numberOfTests + ", ");
            }

            System.out.println("A finalizar o teste da minhash");
            System.out.println("Numero de clashes: " + countClashes);
            double percentage = countClashes * 100 / numberOfTests;
            if (percentage < 4)
                System.out.println("Boa minhash em termos de inteiros || Percentagem de clashes:" + percentage);
            else
                System.out.println("Má minhash em termos de inteiros|| Percentagem de clashes:" + percentage);

            System.out.println("A iniciar o teste da minhash (com os nossos dados)");
        }
    }

    private static LinkedList<Integer> minHash(String s, int numberOfHashes) {
        LinkedList<Integer> set = new LinkedList<Integer>();
        for (int i = 0; i < numberOfHashes; i++) {
            set.add((Integer.parseInt(s)) + i);
        }
        return set;
    }

    public static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }


}

