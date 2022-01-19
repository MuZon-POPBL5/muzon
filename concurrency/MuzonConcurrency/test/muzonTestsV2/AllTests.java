package muzonTestsV2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @file AllTests.java
 * @brief This test suite executes all the test cases of the second solution
 * @author Ander Palacios APalacios
 * @version v1.0.0
 * @date 18/01/2022
*/

@RunWith(Suite.class)
@SuiteClasses(
  {TakerTest.class, LefterTest.class, ItemDepositTest.class, RobotTest.class, BufferTest.class})
public class AllTests
{

}
