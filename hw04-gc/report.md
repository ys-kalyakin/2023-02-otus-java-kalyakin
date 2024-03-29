## Отчет определения оптимального размера хипа

| Размер хипа | Время выполнения |
|-------------|------------------|
| 256         | OOM              |  
| 2048        | 7357             |  
| 4096        | 6765             |
| 8192        | 6641             |
| 1024        | 7884             |
| 3000        | 6701             |
| 2500        | 6828             |
| 16000       | 7000             |

## Выводы
Оптимальным размером хипа является 3gb, при дальнейшем увеличении хипа 
существенного прироста производительности не происходит.

## Отчет определения оптимального размера хипа после выполнения оптимизаций

| Размер хипа | Время выполнения |
|-------------|------------------|
| 3           | 527              |  
| 8           | 525              |  
| 16          | 522              |
| 128         | 522              |
| 256         | 524              |

## Выводы
Оптимальным размером хипа после оптимизации является 4mb, при дальнейшем увеличении хипа 
существенного прироста производительности не происходит.

## Общий вывод из проделанной работы
Для уменьшения размера использования памяти предпочтительно избегать Boxing'a/Unboxing'a 
примитивных типов, где это возможно. Иммутабельным типам для оптимизации предпочитать
мутабельные (но в таком случае получаем сложность или невозможность использования таких типов в 
многопоточной среде). Хранить в локальных переменных/полях класса только минимально необходимую информацию.
