# 2장 입출금 내역 분석기



### 요구 사항

1. 은행 입출금 내역의 총 수입과 총 지출은 각각 얼마인가? 결과가 양수인가 음수인가?
2. 특정 달엔 몇 건의 입출금 내역이 발생했는가?
3. 지출이 가장 높은 상위 10건은 무엇인가?
4. 돈을 가장 많이 소비하는 항목은 무엇인가?





#### KISS 원칙

응용 프로그램 코드를 한 개의 클래스로 구현한다. 예외 처리는 다음 장에서 생각해보자.



```java
public class BankStatementAnalyzerSimple {

    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String[] args) throws Exception {
            final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
            final List<String> lines = Files.readAllLines(path);
            double total = 0;
            for(final String line: lines) {
                String[] columns = line.split(",");
                double amount = Double.parseDouble(columns[1]);
                total += amount;
            }

            System.out.println("The total for all transactions is " + total);
    }
}

```

- 위 코드는 CSV 파일을 응용프로그램의 명령 줄 인수로 전달해 로딩한다.

- Path 클래스는 파일 클래스의 경로를 가리킨다. [Path 클래스 정보](https://dev-box.tistory.com/25)
- Files.readAllLines(path) 로 행 목록을 반환한다

```java
30-01-2017,-100,Deliveroo 
30-01-2017,-50,Tesco 
01-02-2017,6000,Salary 
02-02-2017,2000,Royalties 
02-02-2017,-4000,Rent 
03-02-2017,3000,Tesco 
05-02-2017,-30,Cinema 
```

- 파일의 모든 행을 가져온 다음, 각 행에 다음 작업을 수행한다.
    - 콤마로 열 분리
    - 금액 추출
    - 금액을 double로 파싱
- 한 행에서 금액을 double로 추출해 현재 총합에 더한다. 최종적으로 전체 금액의 합계를 얻는다.



위 코드는 정상 실행되지만, 아래와 같은 문제가 발생할 수 있다. 실제 제품으로 출시되었을 때 발생할 만한 문제를 어떻게 처리할지 고려하는 것이 좋다.

- 파일이 비어 있다면?
- 데이터에 문제가 있어서 금액을 파싱하지 못 한다면?
- 행의 데이터가 완벽하지 않다면?



🚩 특정 달엔 몇 건의 입출금 내역이 발생했는가? 라는 두 번째 문제를 살펴보자. 어떻게 이를 처리할 수 있을까?

위 코드에서 주어진 월을 선택하도록 로직을 바꾼다.

```java
public class BankStatementAnalyzerSimple {

    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String[] args) throws Exception {
            final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
            final List<String> lines = Files.readAllLines(path);
            double total = 0;
        	
        	final DateTimeFormatter DATE_PATTERN = 
                	DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
            for(final String line: lines) {
                final String[] columns = line.split(",");
                final LocalDate date = LocalDate.parse(colums[0], DATE_PATTERN);
                
                if (date.getMonth() == Month.JANUARY) {
                    final double amount = Double.parseDouble(columns[1]);           
                	total += amount;
                }
            }

            System.out.println("The total for all transactions is " + total);
    }
}

```



#### final 변수

예제 코드에서 final 키워드를 잠깐 알아보자. 이 책에서는 상당히 광범위하게 final 키워드를 사용한다. 지역 변수나 필드를 final로 정의하기 때문에 이 변수에 값을 재 할당할 수 없다. final 사용에 따른 장단점이 모두 있으므로 final 사용 여부는 팀과 프로젝트에 따라 달라진다.

- 하지만 final 키워드를 적용한다고 해서 객체가 바뀌지 못하도록 강요하는 것은 아니다. final 필드로 가리키는 객체라도 가변 상태를 포함하기 때문이다. 더욱이 final로 인해 더 많은 코드가 추가된다.
- 자바 언어에서 사용하긴 하지만 final 키워드가 쓸모없는 상황도 있다. 바로 추상 메서드의 메서드 파라미터에 final을 사용하는 상황이다. 이 상황에서는 실제 구현이 없으므로 final 키워드의 의미가 무력화된다. 또한 자바 10에서 var 키워드가 등장하면서 final의 유용성이 크게 감소하였다.



#### 코드 유지 보수성과 안티 패턴

코드를 구현할 때는 코드 유지보수성을 높이기 위해 노력해야 한다. 구현하는 코드가 가졌으면 하는 속성을 목록으로 만들어보자.

- 특정 기능을 담당하는 코드를 쉽게 찾을 수 있어야 한다.
- 코드가 어떤 일을 수행하는지 쉽게 이해할 수 있어야 한다.
- 새로운 기능을 쉽게 추가하거나 기존 기능을 쉽게 제거할 수 있어야 한다.
- **캡슐화**가 잘 되어 있어야 한다. 즉 코드 사용자에게는 세부 구현 내용이 감춰져 있으므로 사용자가 쉽게 코드를 이해하고, 기능을 바꿀 수 있어야 한다.



궁극적으로 개발자의 목표는 현재 만들고 있는 응용프로그램의 복잡성을 관리하는 것이다. 하지만 새로운 요구 사항이 생길 때마다 복사, 붙여넣기로 이를 해결한다면 문제가 생긴다. 이는 효과적이지 않은 해결 방법으로 잘 알려져 있으며, **안티패턴**이라고 한다.

- 한개의 거대한 **갓 클래스**때문에 코드를 이해하기가 어렵다.
- **코드 중복**때문에 코드가 불안정하고 변화에 쉽게 망가진다.

이 두 안티 패턴에 대해 알아보자.



##### 갓 클래스

한 개의 파일에 모든 코드를 구현하다 보면 결국 하나의 거대한 클래스가 탄생하면서 클래스의 목적이 무엇인지 이해하기 어려워진다. 이 거대한 클래스가 모든 일을 수행하기 때문이다. 기존 코드의 로직을 갱신해야 한다면, 어떻게 이 코드를 찾아서 바꿀 수 있을까? 이런 문제를 **갓 클래스 안티패턴**이라 부른다. 한 클래스로 모든 것을 해결하는 패턴이다.



##### 코드 중복

각 문제에서 입력을 읽고 파싱하는 로직이 중복된다. CSV 대신 JSON 파일로 입력 형식이 바뀐다면, 또는 다양한 형식의 파일을 지원해야 한다면 모든 곳의 코드를 다 바꿔야 하며, 새로운 버그가 발생할 가능성이 커진다.

> 여러분은 DRY 원칙을 자주 들어봤을 것이다. 반복을 제거하면 로직을 바꿔도 여러 곳의 코드를 바꿔야 할 필요성이 없어진다.



##### 단일 책임 원칙

**단일 책임 원칙**은 쉽게 관리하고 유지보수하는 코드를 구현하는 데 도움을 주는 포괄적인 소프트웨어 개발 지침이다.



다음 두 가지를 보완하기 위해 SRP를 적용한다.

- 한 클래스는 한 기능만 책임진다.
- 클래스가 바뀌어야 하는 이유는 오직 하나여야 한다.



위 코드의 메인 클래스는 여러 책임을 모두 포함하므로 이를 개별로 분리해야 한다.

1. 입력 읽기
2. 주어진 형식의 입력 파싱
3. 결과 처리
4. 결과 요약 리포트



```java
public class BankStatementCSVParser implements BankStatementParser {

    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BankTransaction parseFrom(final String line) {
        final String[] columns = line.split(",");

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final double amount = Double.parseDouble(columns[1]);

        return new BankTransaction(date, amount, columns[2]);
    }

    public List<BankTransaction> parseLinesFrom(final List<String> lines) {
        return lines.stream().map(this::parseFrom).collect(toList());
    }
}
```

1.  다른문제 구현에 이를 활용할 수 있도록 CSV 파싱 로직을 새로운 클래스로 분리합니다.



> 도메인은 비지니스 문제와 동일한 단어와 용어를 사용한다는 의미다.



BankTransaction 클래스는 응용프로그램의 다른부분에서 입출금 내역 부분이라는 의미를 공유할 수 있어 매우 유용하다.

```java
// 입출금 내역 도메일 클래스

public class BankTransaction {
    private final LocalDate date;
    private final double amount;
    private final String description;


    public BankTransaction(final LocalDate date, final double amount, final String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                date.equals(that.date) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, description);
    }


}
```



#### 응집도

응집도는 코드 구현에서 중요한 특성이다. 단어 자체가 난해하면서도 멋져 보이지만, 실제로 응집도는 코드 유지보수성을 결정하는 중요한 개념이다.

**응집도**는 서로 어떻게 관련되어 있는지를 가리킨다. 정확히 말하자면 응집도는 클래스나 메서드의 책임이 서로 얼마나 강하게 연결되어 있는지를 측정한다.



실무에서는 일반적으로 다음과 같은 여섯 가지 방법으로 그룹화한다.

1. 기능
2. 정보
3. 유틸리티
4. 논리
5. 순차
6. 시간

그룹화하는 메서드의 관련성이 약하면 응집도가 낮아진다.



#### 결합도

코드를 구현할 때 고려해야 할 또 다른중요한 특성으로 **결합도**가 있다. 응집도는 클래스, 패키지, 메서드 등의 동작이 얼마나 관련되어 있는가를 가리키는 반면, 결합도는 한 기능이 다른클래스에 얼마나 의존하고 있는지를 가늠한다.