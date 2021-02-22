Coverage: 62.4%  (Sonarqube) | 67.6% (JUnit: src/main/java)
## TO DO LIST WEB APP PROJECT 

This application consists of an API built with Spring Boot using Java and a frontend created with HTML,CSS and JavaScript.

## Getting Started

Firstly, make sure you have the following programs installed and ideally, Java and Maven are both added to your Path.

### Prerequisites

Git bash - to clone this repo <br />
Java 11/14 - to run the program <br />
Maven  - to run the unit tests <br />
MySQL

### Installing

Clone this repository to your local device. In Git bash:

```
git clone https://github.com/giefortunaQA/TDL_Project.git
```

## Deployment

There is a "TDL-0.0.1-SNAPSHOT.war" file which is executable from your command line. 
Before doing anything, make sure you have the right version of Java by running in your command line:
```
java -version
```

Then, change the directory to your local git repository:
```
cd ..\TDL_Project
```

To start the application, execute the .jar file:
```
java -jar TDL-0.0.1-SNAPSHOT.war
```
When the Spring application is started, go to http://localhost:9090/ to access the web application. To access the h2 database, go to http://localhost:9090/h2-console/.

The above can also be ran without cloning the whole repository. Simply download the .jar file and run the same line, given you're in the directory where file is downloaded to.

Alternaltively, the web app can be launched my opening the index.html file in src/main/resources/static after cloning the repo.

### Running the tests

Before running the tests, make sure the spring boot application is not running. Simply exit the command line and reopen another cmd window. This is because both test and development use the same port 9090. To run the JUnit tests in this Maven package, simply change the directory to ..\20DecSDET2-IMS-Starter and execute
```
mvn test
```

## Mockito Unit Tests 

These tests confirm that each method,without their dependencies, returns the correct/expected value using assertions and mockings. 

```
Example:
//Method in ItemService
	private ItemRepo repo;

	private ModelMapper mapper;

	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}
	public ItemDto create(Item item) {
		return this.mapToIDto(this.repo.save(item));
	}
//Test in ItemServiceTest
	@Autowired
	private ItemService service;
	
	@MockBean
	private ItemRepo repo;
	
	
	private ModelMapper mapper=new ModelMapper();
	
	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}
	@Test
	public void testCreate() throws Exception{
		Item toCreate=new Item("Newly created item",false,prePopList1);
		Item created=new Item(3L,"Newly created item",false,prePopList1);
		//rules
		when(this.repo.save(toCreate)).thenReturn(created);
		//actions
		
		//assertions
		assertThat(this.service.create(toCreate)).isEqualTo(this.mapToIDto(created));
		verify(this.repo,times(1)).save(toCreate);
	}
```
## Spring Integration-System Tests
These are intended to test the  Controller-Service relationships (integration) and asserts that the back-end of the project is functional from Controller to the Sql Database and back (system).
Here, MockMvc and its methods were utilised to send the request and check the response received.

```Example:
  @Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper jsonify;
	
	private ModelMapper mapper=new ModelMapper();
	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}
	
	private String URI = "/item";
	private final ToDoList prePopList1=new ToDoList(1L,"Prepopulated List 1");
	private final ItemDto prePopItem1AsDto=new ItemDto(1L,"Prepopulated Item 1",false);
	private final ItemDto prePopItem2AsDto=new ItemDto(2L,"Prepopulated Item 2",false);
	private final List<ItemDto> prePopItems=List.of(prePopItem1AsDto,prePopItem2AsDto);
	@Test
	public void testDeletePass() throws Exception{
		RequestBuilder request=delete(URI+"/delete/1");
		ResultMatcher confirmStatus=status().isNoContent();
		ResultMatcher confirmBody=content().string("");
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
```

## Acceptance Tests with Selenium
These tests mock a user's interaction with the front-end and asserts that the interaction was successful or otherwise by checking the contents/results of the mock activity.
We use Chrome Driver to retrieve elements of the page and facilitate the actions in the tests. 
At the and of all the tests, an extent report is created in the /target/reports/ folder. This displays the acceptance criteria alongside test failures/successes logically.

```Example
	@Test
	public void testReadListById() throws Exception{
		test=report.startTest("Read list by Id Test");
    //	GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
    //	WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();
		// THEN
		test.log(LogStatus.INFO, "Then - we should see this list and the items in it");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Prepopulated List 1");
		if (contains) {
			test.log(LogStatus.PASS, "List displayed successfully.");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+" instead.");
		}
		assertTrue(contains);
	}
```
## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

All the versions of this project is in Github https://github.com/giefortunaQA/TDL_Project

## Authors

* **Gie-Anne Fortuna** 



## Acknowledgments

*Thanks to Peter and Peprah for their great company and help.
