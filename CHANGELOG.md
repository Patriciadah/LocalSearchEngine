# Iteration 3
## May 22, 2025

-----
### Caching with Proxy pattern

| Class                    | Role                                                      |
| ------------------------ | --------------------------------------------------------- |
| `SearchServiceInterface` | Interface used by both proxy & real service               |
| `RealSearchService`      | The actual implementation (your existing `SearchService`) |
| `CachedSearchProxy`      | Proxy class that caches results                           |
| `SearchController`       | Calls the proxy instead of real service                   |
- Wired CachedSearchProxy with RealSearchProxy through Spring boot framework using `@Configuration` bean

### Upgraded spelling correction

- Introduced Strategy pattern for enforcing Open/Closed principle
- 2 mock strategies : Norvig's strategy and basic dictionary checking
- Attempted to solve 7/10 completely

### Implemented metadata analyzer

- Implemented 9/10 task through class MetadataAnalyzer
- Integrate it in SearchController

### Added Widget logic

- Implemented Factory pattern 
- Defined 2 types of Widgets: CalculatorWidget and SaturnWidget for user query
- Images are displayed if word "calculator" or "saturn" is present in query
- Introduced separation between Special (5/10) checkpoint and Context-Aware (8/10) widgets
- Special: uses query 
- Context-Aware: uses results

## May 21, 2025

------

### Spelling correction helper data
- Added tools package for data formatting
- Added WorldMapBuilder class to extract Peter's Norvig big.txt information
- Serialize results in wordMap.ser file
- Prepare for future use of wordMap.ser
- Backbone for SpellingCorrectionService class inspired by Peter's Norvig algorithm

### Main work: Prepare for frontend migration
- Frontend design choice: React
- Deprecated command line approach
- Sketch main backend controllers 