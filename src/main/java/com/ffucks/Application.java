package com.ffucks;

import com.ffucks.dto.Person;
import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.map.primitive.ObjectDoubleMap;
import org.eclipse.collections.api.partition.list.PartitionMutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Bags;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;

public class Application {
    public static void main(String[] args) {

        MutableList<Person> people = Lists.mutable.with(
                new Person("Ana",   32, java.util.List.of("Java", "K8s"),           "Eng", 12000),
                new Person("John",  27, java.util.List.of("React", "Node"),         "Eng",  9500),
                new Person("Laura", 38, java.util.List.of("Java", "Spring", "SQL"), "Data",14000),
                new Person("Brad",  29, java.util.List.of("Go", "K8s"),             "Eng", 11000)
        );

        //JDK
        java.util.List<String> seniorsJdk = people.stream()
                        .filter(p -> p.age() >= 30)
                        .map(p -> p.name().toUpperCase())
                        .sorted()
                        .toList();

        //Eclipse Collections
        MutableList<String> seniorsEc = people.select(p -> p.age() >= 30)
                        .collect(p -> p.name().toUpperCase())
                        .sortThis();

        //JDK
        java.util.Map<String, java.util.List<Person>> byDeptJdk =
                people.stream().collect(java.util.stream.Collectors.groupingBy(Person::dept));

        //Eclipse Collections
        MutableListMultimap<String, Person> byDeptEc = people.groupBy(Person::dept);

        //Word count JDK
        java.util.Map<String, Long> skillCountJdk =
                people.stream()
                        .flatMap(p -> p.skills().stream())
                        .collect(java.util.stream.Collectors.groupingBy(s -> s,
                                java.util.stream.Collectors.counting()));

        //Word count Eclipse Collections
        Bag<String> skillBag = people.flatCollect(Person::skills, Bags.mutable.empty());
        int javaCount = skillBag.occurrencesOf("Java");

        //FlatMap JDK
        java.util.Set<String> uniqueSkillsJdk =
                people.stream()
                        .flatMap(p -> p.skills().stream())
                        .collect(java.util.stream.Collectors.toSet());

        //Collections
        MutableSet<String> uniqueSkillsEc = people.flatCollect(Person::skills).toSet();

        //Predicate JDK
        java.util.Map<Boolean, java.util.List<Person>> partJdk =
                people.stream().collect(java.util.stream.Collectors.partitioningBy(p -> p.age() >= 30));

        //Predicate Eclipse Collections
        PartitionMutableList<Person> partEc = people.partition(p -> p.age() >= 30);
        MutableList<Person> older   = partEc.getSelected();
        MutableList<Person> younger = partEc.getRejected();

        //SUM and aggregations by key JDK
        java.util.Map<String, Double> sumSalaryByDeptJdk =
                people.stream().collect(
                        java.util.stream.Collectors.groupingBy(
                                Person::dept,
                                java.util.stream.Collectors.summingDouble(Person::salary)
                        )
                );

        //SUM and aggregations by key Eclipse Collections
        ObjectDoubleMap<String> sumSalaryByDeptEc =
                people.sumByDouble(Person::dept, Person::salary);

        double totalEng = sumSalaryByDeptEc.get("Eng");

        //Primitive collections no boxing JDK
        int sumAgesJdk = people.stream().mapToInt(Person::age).sum();

        //Primitive collections no boxing Eclipse Collections
        IntList ages = people.collectInt(Person::age);
        int sumAgesEc = Math.toIntExact(ages.sum());
    }
}
