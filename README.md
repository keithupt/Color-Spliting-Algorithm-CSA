# Ancestral Colorings of Perfect Binary Trees With Applications in Private Retrieval of Merkle Proofs

#### Quang Cao, Rinaldo Gagiano, Duy Huynh, Xun Yi, Son Hoang Dau, Phuc Lu Le, Quang-Hung Luu, Emanuele Viterbo, Yu-Chih Huang, Jingge Zhu, Mohammad M. Jalalzai, and Chen Feng. 2022.

In this paper, we develop a divide-and-conquer algorithm called Color-Splitting Algorithm (CSA) that takes ℎ as input and generates a balanced ancestral coloring for 𝑇 (ℎ) in time linear in the number of tree nodes (excluding the root) 2^(ℎ+1) − 2. In fact, this algorithm can generate not only a balanced ancestral coloring but also any ancestral coloring (color classes having heterogeneous sizes) feasible for 𝑇 (ℎ). It is worth noting that this flexibility of our algorithm establishes the existence of optimal combinatorial patterned-batch codes corresponding to the case of servers with heterogeneous storage capacities as well. At the high level, the algorithm colors two sibling nodes at a time and proceeds recursively down to the two subtrees and repeats the process while maintaining the Ancestral Property: if a color is used for a node then it will not be used for the descendants of that node. The algorithm can produce a balanced ancestral coloring for the tree 𝑇 (20) below 1 second.

Fig 1: An example of the CSA algorithm when h = 5.

<img width="392" alt="Screen Shot 2022-03-04 at 11 57 24 pm" src="https://user-images.githubusercontent.com/87842051/156807275-1a753703-0b56-4077-8a46-2a3dd9cfaceb.png">


Fig 2: An example of the CSA algorithm when h = 20.

<img width="1440" alt="Screen Shot 2022-03-04 at 11 58 49 pm" src="https://user-images.githubusercontent.com/87842051/156807732-1aa578a2-1b03-4b6e-af83-e404dd8f6581.png">


Fig 3: An example of manual input feasible colors with c = [4 4 6].

<img width="865" alt="Screen Shot 2022-03-04 at 11 59 45 pm" src="https://user-images.githubusercontent.com/87842051/156808302-b4c2dd30-9d4c-4a1a-b01c-91ed66046b09.png">


Fig 3: An example of manual input feasible colors with c = [2 4 8].

<img width="863" alt="Screen Shot 2022-03-04 at 11 59 54 pm" src="https://user-images.githubusercontent.com/87842051/156808506-3537fbb0-1771-4b7e-8e30-adc87c37f3ea.png">
