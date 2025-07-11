### JMH Results

## Performance Comparison by Query Type

### Simple Query (Find cars by make = "Tesla")
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| forLoopSimpleQuery | 10.364 | ±0.179 | **1.0x (baseline)** |
| streamSimpleQuery | 10.219 | ±0.095 | **0.99x (fastest)** |
| cqEngineIndexedSimpleQuery | 4.609 | ±0.028 | **0.44x (2.2x faster)** |
| cqEngineSimpleQuery | 60.982 | ±8.084 | 5.9x slower |

### Range Query (Find cars with HP 300-500)
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| streamRangeQuery | 11.807 | ±0.167 | **1.0x (baseline)** |
| forLoopRangeQuery | 11.837 | ±0.129 | 1.0x |
| cqEngineIndexedRangeQuery | 89.361 | ±16.070 | 7.6x slower |
| cqEngineRangeQuery | 138.287 | ±16.545 | 11.7x slower |

### Complex Query (Ford + year ≥ 2020 + HP > 250)
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| forLoopComplexQuery | 10.582 | ±0.256 | **1.0x (baseline)** |
| streamComplexQuery | 13.019 | ±0.283 | 1.2x slower |
| cqEngineIndexedComplexQuery | 15.941 | ±0.120 | 1.5x slower |
| cqEngineComplexQuery | 74.676 | ±1.143 | 7.1x slower |

### String Query (Find cars containing "Sedan")
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| forLoopStringQuery | 90.636 | ±33.439 | **1.0x (baseline)** |
| streamStringQuery | 95.319 | ±9.167 | 1.1x slower |
| cqEngineIndexedStringQuery | 53.323 | ±1.301 | **0.59x (1.7x faster)** |
| cqEngineStringQuery | 187.397 | ±25.537 | 2.1x slower |

### Multi-Value Query (Find cars with "Navigation" option)
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| streamMultiValueQuery | 38.870 | ±2.774 | **1.0x (baseline)** |
| forLoopMultiValueQuery | 41.749 | ±2.585 | 1.1x slower |
| cqEngineIndexedMultiValueQuery | 17.775 | ±0.197 | **0.46x (2.2x faster)** |
| cqEngineMultiValueQuery | 144.628 | ±24.778 | 3.7x slower |

### OR Query (Tesla OR Porsche OR BMW)
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| forLoopOrQuery | 17.500 | ±0.916 | **1.0x (baseline)** |
| cqEngineIndexedOrQuery | 25.235 | ±0.847 | 1.4x slower |
| streamOrQuery | 30.259 | ±0.920 | 1.7x slower |
| cqEngineOrQuery | 100.692 | ±3.383 | 5.8x slower |

### Count Operations (Count Honda cars)
| Implementation | Score (μs/op) | Error | Relative Performance |
|----------------|---------------|-------|---------------------|
| cqEngineIndexedCount | 0.120 | ±0.001 | **1.0x (baseline - 85x faster!)** |
| forLoopCount | 10.246 | ±0.417 | 85.4x slower |
| streamCount | 10.664 | ±0.383 | 88.9x slower |
| cqEngineCount | 61.084 | ±1.091 | 509x slower |

## Key Insights

### 🏆 **CQEngine with Indexes Excels At:**
- **Count Operations**: 85x faster than traditional approaches
- **Simple Equality Queries**: 2.2x faster than for loops
- **Multi-Value Queries**: 2.2x faster than streams
- **String Searches**: 1.7x faster than for loops

### ⚡ **Traditional Approaches Win At:**
- **Range Queries**: For loops and streams are 7-12x faster
- **Complex Multi-Condition Queries**: For loops are fastest
- **OR Queries**: For loops are most efficient

### 📊 **Performance Patterns:**
1. **Indexed CQEngine** shines for exact matches and counting
2. **For loops** are consistently fast across all query types
3. **Streams** perform similarly to for loops but with slightly more overhead
4. **Non-indexed CQEngine** is generally slower due to full collection scanning overhead

### 💡 **Recommendations:**
- Use **CQEngine with indexes** for applications with frequent exact matches, counts, or multi-value searches
- Use **traditional for loops** for simple range queries and complex conditions
- **Avoid non-indexed CQEngine** - it adds overhead without benefits
- Consider **CQEngine** when query complexity increases or when you need advanced querying capabilities
