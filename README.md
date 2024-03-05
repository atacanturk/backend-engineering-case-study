# Backend Engineering Case Study
NOT: I removed the test classes while dockerizing the project because the Maven dependencies were causing issues. I will add them to the repository very soon.
# Project Overview
This project has layered structure. \
  -core:  includes several common aspects(mapping, exception handling)\
  -business: includes services and business logics\
  -dataAccess: responsible for managing the storage and retrieval of data \
  -entity: represents database entities\
  -controller: includes restApi controllers\

Uses spring Data JPA \
  -auto update mode is active\
Exception Handling \
Singleton Design Pattern imlementation thanks to Spring \
# Technical Details\
GroupMatcher is a business layer service that matches tournament participants. It keeps tournament participation requests from different countries in separate queues. Whenever a request arrives, if there is at least one pending request in all queues, it matches these participants.\
