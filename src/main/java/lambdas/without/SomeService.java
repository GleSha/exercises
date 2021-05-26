package lambdas.without;

import lambdas.SomeBusinessLogicEntity;

import java.util.*;

/**
 * Some service class that works with SomeBusinessLogicEntity, there will be many classes like this in application
 */
public class SomeService {

    class FirstComparator implements Comparator<SomeBusinessLogicEntity> {
        @Override
        public int compare(SomeBusinessLogicEntity o1, SomeBusinessLogicEntity o2) {
            return Integer.compare(o1.getSomeParameter(), o2.getSomeParameter());
        }
    }

    public List<SomeBusinessLogicEntity> getEntitiesSortedFirst() {
        List<SomeBusinessLogicEntity> entitiesFromSomeStorage = getEntitiesFromSomeStorage();
        // in this case we should find FirstComparator class definition
        Collections.sort(entitiesFromSomeStorage, new FirstComparator());
        // or
        // in this case we should write 3 more lines of code
        // code in this case looks too verbose, it's not intuitive, not quite obvious
        Collections.sort(entitiesFromSomeStorage, new Comparator<SomeBusinessLogicEntity>() {
            @Override
            public int compare(SomeBusinessLogicEntity o1, SomeBusinessLogicEntity o2) {
                return Integer.compare(o1.getSomeParameter(), o2.getSomeParameter());
            }
        });
        return entitiesFromSomeStorage;
    }


    class SecondComparator implements Comparator<SomeBusinessLogicEntity> {
        @Override
        public int compare(SomeBusinessLogicEntity o1, SomeBusinessLogicEntity o2) {
            if (o1.getSomeName().equals(o2.getSomeName())) {
                return Integer.compare(o1.getSomeProperties().size(), o2.getSomeProperties().size());
            } else if (o1.getSomeName().length() < o2.getSomeName().length()) {
                return (-1) * Integer.compare(o1.getSomeProperties().size(), o2.getSomeProperties().size());
            } else if (o1.getSomeName().length() == o2.getSomeName().length()) {
                return (-1) * Integer.compare(o1.getSomeParameter(), o2.getSomeProperties().size());
            } else {
                return Integer.compare(o1.getSomeParameter(), o2.getSomeParameter());
            }
        }
    }

    public List<SomeBusinessLogicEntity> getEntitiesSortedSecond() {
        List<SomeBusinessLogicEntity> entitiesFromSomeStorage = getEntitiesFromSomeStorage();
        // in this case we should find SecondComparator class definition
        Collections.sort(entitiesFromSomeStorage, new SecondComparator());
        // or
        // in this case we should write 3 more lines of code
        // code in this case looks too verbose, it's not intuitive, not quite obvious
        Collections.sort(entitiesFromSomeStorage, new Comparator<SomeBusinessLogicEntity>() {
            @Override
            public int compare(SomeBusinessLogicEntity o1, SomeBusinessLogicEntity o2) {
                if (o1.getSomeName().equals(o2.getSomeName())) {
                    return Integer.compare(o1.getSomeProperties().size(), o2.getSomeProperties().size());
                } else if (o1.getSomeName().length() < o2.getSomeName().length()) {
                    return (-1) * Integer.compare(o1.getSomeProperties().size(), o2.getSomeProperties().size());
                } else if (o1.getSomeName().length() == o2.getSomeName().length()) {
                    return (-1) * Integer.compare(o1.getSomeParameter(), o2.getSomeProperties().size());
                } else {
                    return Integer.compare(o1.getSomeParameter(), o2.getSomeParameter());
                }
            }
        });
        return entitiesFromSomeStorage;
    }


    private List<SomeBusinessLogicEntity> getEntitiesFromSomeStorage() {
        return new ArrayList<>() {{
            add(new SomeBusinessLogicEntity(1, Arrays.asList(1, 2, 3), "2"));
            add(new SomeBusinessLogicEntity(2, Arrays.asList(1, 3, 4), "asdasd"));
            add(new SomeBusinessLogicEntity(3, Arrays.asList(5, 86, 3), "oiuy"));
            add(new SomeBusinessLogicEntity(2, Arrays.asList(87, -34, 0), "123hjkl"));
            add(new SomeBusinessLogicEntity(6, Arrays.asList(1123, 76, -2), "pov64bvof"));
            add(new SomeBusinessLogicEntity(10, Arrays.asList(123, 9548, 34),"mcod75jf"));
        }};
    }
}
