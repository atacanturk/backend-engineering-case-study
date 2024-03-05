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
# Technical Details
## Group Matching
GroupMatcher is a business layer service that matches tournament participants. It keeps tournament participation requests from different countries in separate queues. Whenever a request arrives, if there is at least one pending request in all queues, it matches these participants.\
## Reward Strategy
If a user passes through a set of business rules, controls are performed according to the RewardStatus enum value of the participant in the database. The logic operates as follows: \
Check the RewardStatus of the participant; \
if it is NOT_CALCULATED, sort everyone in the user's group according to their score, change the status of the first to FIRST, the second to SECOND, and the rest to CLAIMED_OR_NO_REWARD. \
If the requesting user has received a reward, perform the necessary actions, then update the user's status to CLAIMED_OR_NO_REWARD. \
\
Thanks to this strategy, instead of re-ranking the group with every different user's request, the ranking is calculated for other users as well when any user from that group makes a request. This reduces the database access complexity from O(N) to O(N/5) in worst case.\
\
Additionally, this strategy is a lighter method than calling any stored procedure after the tournament ends.\
In a large scale system, instead of performing this calculation for every user after the tournament ends, checking only when a new tournament participation request arrives is a cheaper method.
