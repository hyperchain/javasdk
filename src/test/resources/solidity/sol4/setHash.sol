pragma solidity ^0.5.0;
contract SetHash{
    mapping(string => string) hashMapping;

    function insert(string memory key,string memory value) public {
        hashMapping[key] = value;
    }

    function get(string memory key) public view returns(string memory) {
        return hashMapping[key];
    }
}
