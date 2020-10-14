pragma solidity 0.5.0;
contract Accumulator {
    bytes32[] public test;
    function getTest() public returns (bytes32[] memory) {
        test.push(bytes32(uint256(1)));
        test.push(bytes32(uint256(2)));
        return test;
    }
}
