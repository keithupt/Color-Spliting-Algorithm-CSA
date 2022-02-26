# Ancestral Colorings of Perfect Binary Trees With Applications in Private Retrieval of Merkle Proofs

#### Nhat Quang Cao, Rinaldo Gagiano, Duy Huynh, Xun Yi, Son Hoang Dau, Phuc Lu Le, Quang-Hung Luu, Emanuele Viterbo, Yu-Chih Huang, Jingge Zhu, Mohammad M. Jalalzai, and Chen Feng. 2022.

In this paper, we develop a divide-and-conquer algorithm called Color-Splitting Algorithm (CSA) that takes ℎ as input and generates a balanced ancestral coloring for 𝑇 (ℎ) in time linear in the number of tree nodes (excluding the root) 2^(ℎ+1) − 2. In fact, this algorithm can generate not only a balanced ancestral coloring but also any ancestral coloring (color classes having heterogeneous sizes) feasible for 𝑇 (ℎ). It is worth noting that this flexibility of our algorithm establishes the existence of optimal combinatorial patterned-batch codes corresponding to the case of servers with heterogeneous storage capacities as well. At the high level, the algorithm colors two sibling nodes at a time and proceeds recursively down to the two subtrees and repeats the process while maintaining the Ancestral Property: if a color is used for a node then it will not be used for the descendants of that node. The algorithm can produce a balanced ancestral coloring for the tree 𝑇 (20) around 1 second.

Fig 1: An example of the CSA algorithm when h = 20.
<img width="1440" alt="Screen Shot 2022-01-21 at 1 10 44 am" src="https://user-images.githubusercontent.com/87842051/150396998-9438ddb5-4b18-4ba7-ad32-5ec193dff81f.png">
