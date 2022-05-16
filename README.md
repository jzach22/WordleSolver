# WordleSolver
Uses a Rudimentary limited memory class artificial intelligence to play a wordle like  game and solve it with a 93% accuracy. with the given element that it knows every possible answer.

Usage:
by loading in a library in the same directory the main class calls for a new poolOfchoices generating a pool of potential words to use from the library
while also initializing all potential structure that are needed.

main calls selects a word at random from that list setting that word as the 

then it gathers data of the freqeuncy of all letters relative to the position. then adds up that data for a word then the word with the highest score is attempted

The information from this geuss is then used to eliminate words that cannot be used. then recalculate all letter frequencies relative to position and rinse repeat untill done 
