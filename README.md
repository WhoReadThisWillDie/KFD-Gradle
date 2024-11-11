# Простой gradle плагин для анализа кода

Этот плагин сканирует все java и kotlin файлы, расположенные в `src/main`, и генерирует отчёт следующего вида:
```json
{
  "totalClasses": 8,
  "totalMethods": 5,
  "totalLines": 35
}
```
## Использование
Для запуска используйте команду: 

`./gradlew analyzeCode`
