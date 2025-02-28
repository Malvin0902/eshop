# eshop
https://perfect-rabbit-malv-9750a611.koyeb.app/

https://perfect-rabbit-malv-9750a611.koyeb.app/product/list
## Module 1
## Reflection 1

### Clean Code Principles
- **Meaningful Names**: The names of classes and methods clearly represent their intended function.
- **Functions**: Methods in controllers are dedicated to specific tasks, such as creating or updating a product.
- **Comments**: Minimal use of comments, relying on descriptive names and brief docstrings when needed.
- **Data Structures**: Product objects store only essential information, with clear getter and setter methods.
- **Error Handling**: With bean validation enabled, invalid or missing inputs are handled by early returns, directing users back to the form.

#### Example Code Snippet – Creating a Product
The following example illustrates how we process a new product creation request. Bean validation checks the user input (e.g., ensuring the product name is not empty and the quantity is greater than or equal to 1). If validation fails, the form is returned to the user; otherwise, the product is created and the user is redirected to the product list:

```java
@PostMapping("/create")
public String createProductPost(@Valid @ModelAttribute Product product,
                                BindingResult result,
                                Model model) {
    if (result.hasErrors()) {
        // Return back to the create form if validation fails
        return "createProduct";
    }
    service.create(product);
    return "redirect:list";
}
```

Here is a simplified HTML form (using Thymeleaf) that users complete to create the product. The form fields directly map to the Product class fields:
```html
<form th:action="@{/product/edit}" th:object="${product}" method="post">
        <input type="hidden" th:field="*{productId}"/>
        <div class="form-group">
            <label for="nameInput">Name</label>
            <input th:field="*{productName}" type="text" id="nameInput" class="form-control mb-4 col-4">
        </div>
        <div class="form-group">
            <label for="quantityInput">Quantity</label>
            <input th:field="*{productQuantity}" type="text" id="quantityInput" class="form-control mb-4 col-4">
        </div>
        <button type="submit" class="btn btn-success">Update</button>
    </form>
```
By combining meaningful names, concise validation, and straightforward form rendering, we keep our code both readable and reliable.

## Secure Coding Practices
- **ID Generation:** If no ID is provided, a UUID is generated to avoid ID duplication.

- **Validation (Future Enhancement):** Extend bean validation with more specific constraints.

- **Authorization (Future):** Restrict certain endpoints to authorized users.

- **Output Data Encoding (Future):** Implement encoding for dynamically rendered fields to prevent XSS vulnerabilities.

## Possible Improvements
- Add more detailed exception handling for edge cases.
- Increase test coverage (unit and functional tests) to ensure that existing features are not broken by changes.

# Reflection 2
After adding unit tests, I feel way more confident about how reliable the app is. The tests serve as a kind of early warning—if something changes or breaks, the tests will catch it fast. I usually figure out how many tests to write based on how complex the class is and all the different scenarios we need to check. Code coverage tools can help spot untested lines or paths, but just having 100% coverage doesn’t necessarily mean the code is bug-free.
For our new functional test suite, which checks things like creating products and counting them on the list page, here are some important points for keeping the code clean:

1. **Potential Duplication**

- If the setup or teardown logic repeats too much in test classes, it gets messy.
- It’s easier to maintain when we put common steps in a base test class or helper methods.

2. **Hard-Coded Values and Shared State**

- Using fixed values too often (like product IDs or names) can make the code harder to read.
- Generating random or parameterized test data can make tests clearer and more robust.

3. **Single Responsibility in Tests**

- Tests that check multiple things at once can be confusing.
- It’s better when each test focuses on one thing. This way, debugging is faster if a test fails.

4. **Readability and Naming**

- Test names should be clear and descriptive (e.g., shouldCreateProduct_whenValidData).
- Using consistent naming conventions helps keep the tests easy to understand.

5. **Test Data Management**

- Utility methods to create or reset sample data reduce repetition and keep the “Arrange, Act, Assert” structure clean.
- Repeated creation steps should be refactored into @BeforeEach or helper methods.

## Suggestions for Improvement:

- **Use a Base Functional Test Class:** Put common logic (like driver setup or form-filling steps) into a parent class.
- **Maintain Meaningful Test Names:** The test name should explain what’s being verified and the expected outcome.
- **Don’t Rely on Coverage Alone:** Use coverage to find untested parts, but remember it doesn’t mean everything’s fully tested just because the code runs.

## Module 2
### Reflection 
#### Fixed Code Quality Issues
1. JUnit 5 Test Method Naming Convention:
   
Issue: The JUnit 5 test methods testFindById_NotFound and pageTitle_isCorrect didn't adhere to the naming convention [a-z][a-zA-Z0-9]*, meaning they had underscores in the method name. 

Fix: Renamed the methods to use camel case and removed underscores,  
such as:
  testFindById_NotFound → testFindByIdNotFound

2. Unused Static Import Warning:

Issue: The static import org.junit.jupiter.api.Assertions.* was flagged as unused.  

Fix: Modified it to import only the specific assertion methods that were being used in the test (such as assertNotNull, assertEquals, etc.).


#### CI/CD Workflows 


Looking at the CI/CD workflows (GitHub Actions in this case), I think the current setup totally fits the definition of Continuous Integration (CI) and Continuous Deployment (CD).

##### Continuous Integration (CI): 
With GitHub Actions, every time there’s a push or pull request to any branch, the workflows automatically run. This helps make sure that all code changes are tested and integrated continuously, avoiding any integration issues.

##### Continuous Deployment (CD): 
For deployment, Koyeb takes care of it automatically. Whenever there’s a push or pull request, the project gets deployed right away, so the latest updates are live without having to do anything manually.

In short, everything’s set up to keep things automated, from testing and integrating code to pushing updates straight to production.

![img.png](img.png)

# Module 3
# SOLID Principles in My Project

## What SOLID Principles Did I Apply?

In this project, I used three main SOLID principles to make things more maintainable, flexible, and easier to scale. Here’s what I used:

- **Single Responsibility Principle (SRP)**
- **Open/Closed Principle (OCP)**
- **Liskov Substitution Principle (LSP)**

### 1. **Single Responsibility Principle (SRP)**

**What is SRP?**
- SRP says that a class or module should only have one responsibility. It should only have one reason to change.

**How I applied it:**
- Originally, `ProductController.java` was doing both product and car-related logic. I split them by moving the `CarController` class (which was extending `ProductController`) into a new file, called `CarController.java`. Now, each controller has its own job.

**Before (SRP Violation):**
```java
// ProductController.java
@Controller
@RequestMapping("/car")
class CarController extends ProductController {
    @Autowired
    private CarServiceImpl carservice;

    public CarController(ProductService service) {
        super(service);
    }
}
```
After (SRP Applied):

```java

// CarController.java
@Controller
@RequestMapping("/car")
public class CarController {
    private final CarService carservice;

    public CarController(CarService carService) {
        this.carservice = carService;
    }
}
```
2. Open/Closed Principle (OCP)
What is OCP?

OCP says that classes should be open for extension, but closed for modification. This means we can add new features without changing existing code.
How I applied it:

Instead of modifying services every time I add a new repository, I created interfaces like ICarRepository.java and IProductRepository.java. This way, new repositories can be added without touching the old ones.
Before (OCP Violation):

```java
@Repository
public class CarRepository {
    static int id = 0;
    private List<Car> carData = new ArrayList<>();
    // CRUD operations
}
```
After (OCP Applied):

```java
@Repository
public class CarRepository implements ICarRepository {
    static int id = 0;
    private List<Car> carData = new ArrayList<>();
    // CRUD operations
}
```
3. Liskov Substitution Principle (LSP)
What is LSP?

LSP says that you should be able to replace a parent class with a subclass without breaking the program. Subclasses should behave in a way that’s expected by their parent.
How I applied it:

I created a generic IRepository<T> interface that all repositories implement. This helps keep everything consistent and makes it easier to swap one repository for another without causing issues.
Before (LSP Violation):

```java
public interface ICarRepository {
    Iterator<Car> findAll();  // Returns Iterator
}

public interface IProductRepository {
    List<Product> findAll();  // Returns List
}
```
After (LSP Applied):

```java
public interface IRepository<T> {
    T create(T entity);
    Iterator<T> findAll();
    T findById(String id);
    T update(String id, T entity);
    void delete(String id);
}

public interface ICarRepository extends IRepository<Car> {}
public interface IProductRepository extends IRepository<Product> {}
``` 
Benefits of Using SOLID Principles
1. Easier to Modify and Scale
When you use SOLID principles, especially OCP, it becomes a lot easier to add new features without messing with the old code.

Example:

If I want to add a new entity (e.g., Motorcycle), I just create new interfaces and repositories (IBikeRepository, BikeRepository) without touching the existing services or repositories. This way, the old code is safe from bugs.
Before (OCP Violation):

```java
@Repository
public class CarRepository {
    static int id = 0;
    private List<Car> carData = new ArrayList<>();
    // CRUD operations
}
```
After (OCP Applied):

```java
@Repository
public class CarRepository implements ICarRepository {
    static int id = 0;
    private List<Car> carData = new ArrayList<>();
    // CRUD operations
}
```
2. More Flexible Code
LSP makes sure that all the repositories behave the same way, so you can swap them in and out without breaking anything.

Before (LSP Violation):

```java
public interface ICarRepository {
    Iterator<Car> findAll();  // Returns Iterator
}

public interface IProductRepository {
    List<Product> findAll();  // Returns List
}
```
After (LSP Applied):

```java
public interface IRepository<T> {
    Iterator<T> findAll();
}
```
3. Improved Readability and Maintainability
SRP makes the code cleaner and easier to work with. When each class has a single responsibility, it’s easier to understand and modify.

Example:

Before, ProductController.java was handling both Product and Car. After SRP, each class only handles one thing.
Before (SRP Violation):

```java
@Controller
@RequestMapping("/car")
class CarController extends ProductController {
    // Handles both Product and Car logic
}
```
After (SRP Applied):

```java
@Controller
@RequestMapping("/car")
public class CarController {
    private final CarService carservice;
    
    public CarController(CarService carService) {
        this.carservice = carService;
    }
}
```
Problems if You Don't Use SOLID Principles
1. Harder to Extend (OCP Violation)
Without OCP, adding new features or entities means you’ll have to modify existing code, which can lead to bugs or unexpected behavior.

Example:

Without OCP, if you want to add a new repository (like MotorcycleRepository), you’ll need to change existing services like CarServiceImpl and ProductServiceImpl. This increases the risk of bugs.
2. Inconsistent and Error-Prone Code (LSP Violation)
If repositories return different types of data, the service layer will have to handle each case separately. This creates confusion and makes the code prone to errors.

Example:

If findAll() in CarRepository returns an Iterator, and ProductRepository returns a List, the service layer will have to deal with them differently, leading to bugs and inconsistent behavior.
3. Code Becomes Harder to Maintain (SRP Violation)
Without SRP, classes get too big and do too much. This makes the code harder to understand and update.

Example:

If ProductController handles both Product and Car, any change to one part could break the other. SRP helps avoid that by giving each class its own job.
Conclusion
By applying the SOLID principles (SRP, OCP, and LSP), I’ve made the project easier to scale, modify, and maintain. These principles allow us to add new features and fix bugs without breaking existing functionality, making the code more stable and flexible.