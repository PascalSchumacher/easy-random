package io.github.benas.jpopulator.beans;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java bean used for tests.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class SocialPerson extends Person {

    private Set<Person> friends;

    private List<String> pseudos;

    private Map<String, String> accounts;

    public SocialPerson() {
    }

    public Set<Person> getFriends() {
        return friends;
    }

    public void setFriends(Set<Person> friends) {
        this.friends = friends;
    }


    public List<String> getPseudos() {
        return pseudos;
    }

    public void setPseudos(List<String> pseudos) {
        this.pseudos = pseudos;
    }

    public Map<String, String> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, String> accounts) {
        this.accounts = accounts;
    }

}
