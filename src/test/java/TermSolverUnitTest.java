import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TermSolverUnitTest {

    @Test
    public void transformInfixToPostfix_ShouldReturnCorrectlyTransformedPostfixTerm() {
        Map<String, String> expectations = new HashMap<>(){
            {
                put("A*B+C", "A B * C +");
                put("A+B*C", "A B C * +");
                put("A*(B+C)", "A B C + *");
                put("A-B+C", "A B - C +");
                put("A*B^C+D", "A B C ^ * D +");
                put("A*(B+C*D)+E", "A B C D * + * E +");
                put("AB*CD+EF", "AB CD * EF +");
                put("(3+4)*(5-6)", "3 4 + 5 6 - *");
                put("50+50-25*0+2+2", "50 50 + 25 0 * - 2 + 2 +");
            }
        };

        // Expect for every term above, that the Optional contains a value and is equal to the expected output
        for(Map.Entry<String, String> entry : expectations.entrySet()) {
            Optional<String> transformed = TermSolver.transformInfixToPostfix(entry.getKey());
            assertTrue(transformed.isPresent());
            assertEquals(entry.getValue(), transformed.get());
        }
    }

    @Test
    public void solvePostfix_ShouldReturnCorrectNumber() {
        Map<String, Double> expectations = new HashMap<>(){
            {
                put("23 2 * 4 -", 42.0D);
                put("3 4 / 9 3 + * 3 + 1 4 - -", 15.0D);
                put("50 50 + 25 0 * - 2 + 2 +", 104.0D);
            }
        };

        for(Map.Entry<String, Double> entry : expectations.entrySet()) {
            assertEquals(entry.getValue(), TermSolver.solvePostfix(entry.getKey()), 0.0D);
        }
    }

    @Test
    public void solvePostfix_ShouldThrowNumberFormatException_IfInputIsMalformed() {
        // Expect a malformed input to throw a NumberFormatException
        try {
            TermSolver.solvePostfix("hello");
            fail("No NumberFormatException");
        } catch (NumberFormatException ignored) {
        }

        Optional<String> transformed = TermSolver.transformInfixToPostfix("2**31");
        try {
            transformed.ifPresent(TermSolver::solvePostfix);
            fail("No NumberFormatException");
        } catch (NumberFormatException ignored) {
        }
    }

}
