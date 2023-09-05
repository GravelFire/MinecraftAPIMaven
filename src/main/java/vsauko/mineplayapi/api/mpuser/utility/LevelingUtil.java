package vsauko.mineplayapi.api.mpuser.utility;

import lombok.experimental.UtilityClass;
import vsauko.mineplayapi.api.utility.PercentUtil;

@UtilityClass
public class LevelingUtil {

  double BASE = 828;
  double GROWTH = 500;

  double HALF_GROWTH = 0.5 * GROWTH;
  double REVERSE_PQ_PREFIX = -(BASE - HALF_GROWTH) / GROWTH;
  double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
  double GROWTH_DIVIDES_2 = 2 / GROWTH;

  /**
   * Этот метод возвращает уровень игрока
   *
   * @param exp Опыт из которого надо получить опыт
   * @return Уровень, в виде целого числа
   */
  public int getLevel(double exp) {
    return exp < 0 ? 1 : (int) Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * exp));
  }

  /**
   * Этот метод возвращает уровень игрока, но уже в виде дроби
   *
   * @param exp Опыт из которого надо получить опыт
   * @return Уровень, в виде дробного числа
   */
  public double getExactLevel(double exp) {
    return LevelingUtil.getLevel(exp) + LevelingUtil.getPercentageToNextLevel(exp);
  }

  /**
   * Этот метод возвращает количество опыта от уровня
   *
   * @param level Уровень который мы хотим проверить
   * @return Количество опыта, необходимого набрать
   */
  public double getExpFromLevelToNext(double level) {
    return level < 1 ? BASE : GROWTH * (level - 1) + BASE;
  }

  /**
   * Этот метод возвращает количество опыта, необходимое для перехода с уровня на уровень
   *
   * @param level Уровень который мы хотим проверить, принимает дробные значения
   * @return Количество опыта, необходимого набрать
   */
  public double getTotalExpToLevel(double level) {
    double lv = Math.floor(level), x0 = LevelingUtil.getTotalExpToFullLevel(lv);
    if (level == lv) return x0;
    return (LevelingUtil.getTotalExpToFullLevel(lv + 1) - x0) * (level % 1) + x0;
  }

  public double getTotalExpToFullLevel(double level) {
    return (HALF_GROWTH * (level - 2) + BASE) * (level - 1);
  }

  /**
   * Этот метод отдаст вам процент до следующего уровня
   * @param exp Опыт из которого хотим получить процент
   * @return Процент до 2х запятых после точки
   */
  public double getPercentageToNextLevel(double exp) {
    double lv = LevelingUtil.getLevel(exp), x0 = getTotalExpToLevel(lv);

    return PercentUtil.getPercent(exp-x0, LevelingUtil.getTotalExpToLevel(lv + 1) - x0);
  }
}