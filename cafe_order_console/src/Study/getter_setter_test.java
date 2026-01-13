package Study;

public class getter_setter_test {
    public static void main(String[] args) throws Exception {
        // 초기화 - 각각 수동 초기화 (실수)
        Human human0 = new Human();
        human0.setAge(10);
        human0.setName("이름0");
        System.out.println("human0 = " + human0);

        // 초기화1 - 각각 수동 초기화 (성공)
        Human human1 = new Human();
        human1.setAge(20);
        human1.setName("이름1");
        human1.setJob(new Human.Job("직업1"));

        // 초기화2 - 메서드로 초기화
        Human human2 = new Human();
        human2.initHuman(30, "이름2", new Human.Job("직업2"));

        // 초기화3 - 생성자로 초기화
        Human human3 = new Human(40, "이름3", new Human.Job("직업3"));

        // 출력
        System.out.println("human1 = " + human1);
        System.out.println("human2 = " + human2);
        System.out.println("human3 = " + human3);


    }

    static class Human {

        private int age;
        private String name;
        private Job job;

        public Human() {
        }

        public Human(int age, String name, Job job) {
            this.age = age;
            this.name = name;
            this.job = job;
        }

        public void initHuman(int age, String name, Job job) throws Exception {
            this.age = age;
            this.name = name;
            this.job = job;
        }



        public int getAge() {
            return age;
        }


        public void setAge(int age) throws Exception {
            if (age <= 0) {
                throw new Exception();
            }

            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Job getJob() {
            return job;
        }

        public void setJob(Job job) {
            this.job = job;
        }

        @Override
        public String toString() {
            return "Human{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", job=" + job +
                    '}';
        }

        static class Job {
            private String name;

            public Job(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return "Job{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }
    }
}
