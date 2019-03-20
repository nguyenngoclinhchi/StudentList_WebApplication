package com.student.backend;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class StaticData {
    private static final String ENGLISH = "English";
    private static final String MATHEMATICS = "Mathematics";
    private static final String CHEMISTRY = "Chemistry";
    private static final String PHYSICS = "Physics";
    private static final String BIOLOGY = "Biology";
    private static final String GEOGRAPHY = "Geography";
    private static final String SOCIAL_STUDIES = "Social Studies";
    private static final String MUSIC = "Music";
    private static final String OTHERS = "Other";
    public static final String UNDEFINED = "Undefined";

    public static final String[] SUBJECT = {ENGLISH, MATHEMATICS, CHEMISTRY, PHYSICS, BIOLOGY, GEOGRAPHY,
            SOCIAL_STUDIES, MUSIC, OTHERS};
    public static final String[] FIRST_NAMES = {
            "Abigail", "Ava", "Elizabelth", "Emily", "Emma", "Isabella", "Madison", "Mia",
            "Olivia", "Sophia", "Alessia", "Alice", "Aurora", "Giorgia", "Martina", "Leonie",
            "Marie", "Daniela", "Lucia", "Martina", "Sara", "Antonella", "Antonia", "Catalina",
            "Emilia", "Fernanda", "Florence", "Isidora", "Martina", "Sofia", "Valentina", "Ayse",
            "Busra", "Elif", "Emine", "Esra", "Fatma", "Irem", "Kubra", "Merve", "Zeynep",
            "Liam", "Noah", "William", "James", "Logan", "Benjamin", "Mason", "Elijah",
            "Oliver", "Jacob", "Lucas", "Michael", "Alexander", "Ethan", "Daniel", "Matthew",
            "Aiden", "Henry", "Joseph", "Jackson", "Samuel", "Sebastian", "David", "Carter",
            "Wyatt", "Jayden", "John", "Owen", "Dylan", "Luke", "Gabriel", "Anthony", "Isaac",
            "Grayson", "Jack", "Julian", "Levi", "Christopher", "Joshua", "Andrew", "Lincoln",
            "Mateo", "Ryan", "Jaxon", "Nathan", "Aaron", "Isaiah", "Thomas", "Charles", "Caleb",
            "Josiah", "Christian", "Hunter", "Eli", "Jonathan", "Connor", "Landon", "Adrian",
            "Asher", "Cameron", "Leo", "Theodore", "Jeremiah", "Hudson", "Robert", "Easton",
            "Nolan", "Nicholas", "Ezra", "Colton", "Angel", "Brayden", "Jordan", "Dominic",
            "Austin", "Ian", "Adam", "Elias", "Jaxson", "Greyson", "Jose", "Ezekiel", "Carson",
            "Evan", "Maverick", "Bryson", "Jace", "Cooper", "Xavier", "Parker", "Roman", "Jason",
            "Santiago", "Chase", "Sawyer", "Gavin", "Leonardo", "Kayden", "Ayden", "Jameson",
            "Kevin", "Bentley", "Zachary", "Everett", "Axel", "Tyler", "Micah", "Vincent",
            "Weston", "Miles", "Wesley", "Nathaniel", "Harrison", "Brandon", "Cole", "Declan",
            "Luis", "Braxton", "Damian", "Silas", "Tristan", "Ryder", "Bennett", "George",
            "Emmett", "Justin", "Kai", "Max", "Diego", "Luca", "Ryker", "Carlos", "Maxwell",
            "Kingston", "Ivan", "Maddox", "Juan", "Ashton", "Jayce", "Rowan", "Kaiden",
            "Giovanni", "Eric", "Jesus", "Calvin", "Abel", "King", "Camden", "Amir", "Blake",
            "Alex", "Brody", "Malachi", "Emmanuel", "Jonah", "Beau", "Jude", "Antonio", "Alan",
            "Elliott", "Elliot", "Waylon", "Xander", "Timothy", "Victor", "Bryce", "Finn",
            "Brantley", "Edward", "Abraham", "Patrick", "Grant", "Karter", "Hayden", "Richard",
            "Miguel", "Joel", "Gael", "Tucker", "Rhett", "Avery", "Steven", "Graham", "Kaleb",
            "Jasper"};
    public static final String[] LAST_NAME = {
            "SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER", "WILSON",
            "MOORE", "TAYLOR", "ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN",
            "THOMPSON", "GARCIA", "MARTINEZ", "ROBINSON", "CLARK", "RODRIGUEZ", "LEWIS", "LEE",
            "WALKER", "HALL", "ALLEN", "YOUNG", "HERNANDEZ", "KING", "WRIGHT", "LOPEZ",
            "HILL", "SCOTT", "GREEN", "ADAMS", "BAKER", "GONZALEZ", "NELSON", "CARTER",
            "MITCHELL", "PEREZ", "ROBERTS", "TURNER", "PHILLIPS", "CAMPBELL", "PARKER", "EVANS",
            "EDWARDS", "COLLINS", "STEWART", "SANCHEZ", "MORRIS", "ROGERS", "REED", "COOK",
            "MORGAN", "BELL", "MURPHY", "BAILEY", "RIVERA", "COOPER", "RICHARDSON", "COX",
            "HOWARD", "WARD", "TORRES", "PETERSON", "GRAY", "RAMIREZ", "JAMES", "WATSON",
            "BROOKS", "KELLY", "SANDERS", "PRICE", "BENNETT", "WOOD", "BARNES", "ROSS",
            "HENDERSON", "COLEMAN", "JENKINS", "PERRY", "POWELL", "LONG", "PATTERSON", "HUGHES",
            "FLORES", "WASHINGTON", "BUTLER", "SIMMONS", "FOSTER", "GONZALES", "BRYANT",
            "ALEXANDER", "RUSSELL", "GRIFFIN", "DIAZ", "HAYES"};
    public static final String[] CLASS_NAME = {
            "A0170767", "A0331603", "A0343164", "A0331605", "A0331805", "A0331602", "A0331685", "A0133605",
            "A0334605", "A0331705", "A0361605", "A0331205", "A0331695"};
    public static class Element {
        private String firstName;
        private String lastName;
        private String subject;
        private String className;

        public Element(String firstName, String lastName, String subject, String className) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.subject = subject;
            this.className = className;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
    public static class ElementList {
        private List<Element> LIST_STUDENTDS;

        public ElementList() {
            this.LIST_STUDENTDS = new ArrayList<>();
            for (int i = 0; i < FIRST_NAMES.length; i++) {
                int j = i % (LAST_NAME.length);
                int k = i % (SUBJECT.length);
                int l = i % (CLASS_NAME.length);
                LIST_STUDENTDS.add(new Element(FIRST_NAMES[i], LAST_NAME[j], SUBJECT[k], CLASS_NAME[l]));
            }
            LIST_STUDENTDS.add(new Element("Ngoc Linh Chi", "NGUYEN", MATHEMATICS, CLASS_NAME[0]));
            LIST_STUDENTDS.add(new Element("Minh", "NGUYEN", CHEMISTRY, CLASS_NAME[1]));
            LIST_STUDENTDS.add(new Element("Guang Rong", "BUNNY", CHEMISTRY, CLASS_NAME[2]));
            LIST_STUDENTDS.add(new Element("", "", UNDEFINED, UNDEFINED));
        }

        public List<Element> getSampleStudentList() {
            return LIST_STUDENTDS;
        }
    }
    /** This class is not meant to be instantiated. */
    private StaticData() {
    }
}
