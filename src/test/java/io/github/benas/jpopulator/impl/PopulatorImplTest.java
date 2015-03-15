/*
 * The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.jpopulator.impl;

import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.beans.*;
import io.github.benas.jpopulator.randomizers.CityRandomizer;
import io.github.benas.jpopulator.randomizers.EmailRandomizer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the {@link Populator} implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class PopulatorImplTest {

    /**
     * The populator to test.
     */
    private Populator populator;

    @Before
    public void setUp() throws Exception {
        populator = new PopulatorBuilder().build();
    }

    @Test
    public void generatedBeanShouldBeCorrectlyPopulated() throws Exception {
        Person person = populator.populateBean(Person.class);

        assertPerson(person);
    }

    @Test
    public void excludedFieldsShouldNotBePopulated() throws Exception {
        Person person = populator.populateBean(Person.class, "name");

        assertThat(person).isNotNull();
        assertThat(person.getName()).isNull();
    }

    @Test
    public void finalFieldsShouldNotBePopulated() throws Exception {
        Person person = populator.populateBean(Person.class);

        assertThat(person).isNotNull();
        assertThat(person.getId()).isNull();
    }

    @Test
    public void generatedBeansListShouldNotBeEmpty() throws Exception {
        List<Person> persons = populator.populateBeans(Person.class);

        assertThat(persons).isNotNull().isNotEmpty();
    }

    @Test
    public void generatedBeansShouldBeCorrectlyPopulated() throws Exception {
        List<Person> persons = populator.populateBeans(Person.class);
        for (Person person : persons) {
            assertPerson(person);
        }
    }

    @Test
    public void excludedFieldsOfGeneratedBeansShouldNotBePopulated() throws Exception {
        List<Person> persons = populator.populateBeans(Person.class, "name");
        for (Person person : persons) {
            assertThat(person).isNotNull();
            assertThat(person.getName()).isNull();
        }
    }

    @Test
    public void generatedBeansNumberShouldBeEqualToSpecifiedNumber() throws Exception {
        List<Person> persons = populator.populateBeans(Person.class, 2);
        assertThat(persons).isNotNull().hasSize(2);
        for (Person person : persons) {
            assertPerson(person);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenThenSpecifiedNumberOfBeansToGenerateIsNegativeThenShouldThrowAnIllegalArgumentException() throws Exception {
        populator.populateBeans(Person.class, -2);
    }

    @Test
    public void generatedBeansWithCustomRandomizersShouldBeCorrectlyPopulated() {
        populator = new PopulatorBuilder()
                .registerRandomizer(Person.class, String.class, "email", new EmailRandomizer())
                .registerRandomizer(Address.class, String.class, "city", new CityRandomizer())
                .build();

        Person person = populator.populateBean(Person.class);

        assertThat(person).isNotNull();

        assertThat(person.getEmail()).isNotNull().isNotEmpty();

        final Address address = person.getAddress();
        assertThat(address).isNotNull();
        assertThat(address.getCity()).isNotNull().isNotEmpty();
    }

    @Test
    public void testExclusionViaAnnotation() {
        Person person = populator.populateBean(Person.class);

        assertThat(person).isNotNull();
        assertThat(person.getExcluded()).isNull();
    }

    @Test
    public void testJavaNetTypesPopulation() throws Exception {

        Website website = populator.populateBean(Website.class);

        assertThat(website).isNotNull();
        assertThat(website.getName()).isNotNull();
        assertThat(website.getUri()).isNotNull();
        assertThat(website.getUrl()).isNotNull();

    }

    @Test
    public void testCollectionPopulation() {
        SocialPerson socialPerson = populator.populateBean(SocialPerson.class);

        assertThat(socialPerson).isNotNull();
        assertPerson(socialPerson);

        final Set<Person> friends = socialPerson.getFriends();
        assertThat(friends).isNotNull();
        for (Person friend : friends) {
            assertThat(friend).isNotNull();
            assertPerson(friend);
        }

        final List<String> pseudos = socialPerson.getPseudos();
        assertThat(pseudos).isNotNull();
        for (String pseudo : pseudos) {
            assertThat(pseudo).isNotNull();
        }

        final Map<String,String> accounts = socialPerson.getAccounts();
        assertThat(accounts).isNotNull();
        for (Map.Entry<String, String> account : accounts.entrySet()) {
            assertThat(account.getKey()).isNotNull();
            assertThat(account.getValue()).isNotNull();
        }
    }

    /*
     * Assert that a person is correctly populated
     */
    private void assertPerson(Person person) {
        assertThat(person).isNotNull();
        assertDeclaredFields(person);
        assertInheritedFields(person);
        assertNestedTypes(person);
    }

    /*
     * Assert that declared fields are populated
     */
    private void assertDeclaredFields(Person person) {

        assertThat(person.getEmail()).isNotNull().isNotEmpty();

        assertThat(person.getGender()).isNotNull().isIn(Arrays.asList(Gender.MALE, Gender.FEMALE));

        assertThat(person.getBirthDate()).isNotNull();

        assertThat(person.getPhoneNumber()).isNotNull().isNotEmpty();

        assertThat(person.getNicknames()).isNotNull();
    }

    /*
     * Assert that inherited fields are populated
     */
    private void assertInheritedFields(Person person) {
        assertThat(person.getName()).isNotNull().isNotEmpty();
    }

    /*
     * Assert that fields of complex types are recursively populated (deep population)
     */
    private void assertNestedTypes(Person person) {

        final Address address = person.getAddress();
        assertThat(address).isNotNull();
        assertThat(address.getCity()).isNotNull().isNotEmpty();
        assertThat(address.getCountry()).isNotNull().isNotEmpty();
        assertThat(address.getZipCode()).isNotNull().isNotEmpty();

        final Street street = address.getStreet();
        assertThat(street).isNotNull();
        assertThat(street.getName()).isNotNull().isNotEmpty();
        assertThat(street.getNumber()).isNotNull();
        assertThat(street.getType()).isNotNull();

    }

}
