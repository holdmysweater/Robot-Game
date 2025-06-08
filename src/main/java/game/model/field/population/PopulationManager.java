package game.model.field.population;

import game.model.field.core.FieldObject;
import game.model.field.core.Mole;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Выполняет задачу управления популяциями объектов поля {@link game.model.field.core.CellObject}
 */
public class PopulationManager {

    //region КОНСТРУКТОРЫ

    /**
     * Создаёт популяции объектов.
     * Определяет порядок обновления популяций.
     */
    public PopulationManager() {
        for (Class<? extends Population> populationClass : populationClasses) {
            try {
                populationList.add(populationClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //endregion

    //region ПОПУЛЯЦИИ

    /**
     * Таблица соответствий типа объекта с его популяцией {@link Population}.
     */
    private static final Map<@NotNull Class<?>, @NotNull Class<? extends Population>> pairsOfPopulationsAndTheirObjects = Map.ofEntries(
            Map.entry(Mole.class, PopulationMole.class)
    );

    /**
     * Список классов популяций {@link Population}, расположенных в порядке их обновления в игре.
     */
    private static final List<Class<? extends Population>> populationClasses = List.of(
            PopulationMole.class
    );

    /**
     * Список экземпляров популяций {@link Population} в порядке их обновления в игре.
     */
    private List<Population> populationList = new ArrayList<>();

    /**
     * Получить неизменяемый список популяций, расположенных в порядке обновления.
     * @return список популяций.
     */
    public List<Population> getPopulations() {
        return Collections.unmodifiableList(populationList);
    }

    /**
     * Получить экземпляр класса популяции по его типу.
     *
     * @param populationClass класс популяции.
     * @return экземпляр популяции.
     */
    private Population getPopulation(Class<? extends Population> populationClass) {
        for (Population population : populationList) {
            if (population.getClass().equals(populationClass)) {
                return population;
            }
        }
        return null;
    }

    //endregion

    //region ОБНОВЛЕНИЕ

    /**
     * Обновляет популяции.
     */
    public void update() {
        for (Population population : populationList) {
            population.update();
        }
    }

    //endregion


    /**
     * Добавить объект в популяции.
     * Объект может быть добавлен в несколько популяций.
     * Если не существует популяции для этого объекта, то вернётся Ложь, иначе вернётся Правда.
     *
     * @param object объект.
     */
    public void addObject(@NotNull FieldObject object) {
        // Для всех известных популяций
        for (Map.Entry<Class<?>, Class<? extends Population>> entry : pairsOfPopulationsAndTheirObjects.entrySet()) {
            Class<?> populationObject = entry.getKey(); // Класс объекта популяции
            Class<? extends Population> populationClass = entry.getValue(); // Класс популяции

            // Добавить объект в популяцию, если он к ней относится
            if (populationObject.isInstance(object)) {
                // Получить экземпляр популяции этого типа объектов
                Population population = this.getPopulation(populationClass);

                // Проверка корректности данных
                assert population == null: "Population for class " + populationClass.getSimpleName() + " must not be null. Ensure that the population is properly initialized.";
                if (population == null) continue;

                // Добавить объект в популяцию
                population.addObject(object);
            }
        }
    }
}
