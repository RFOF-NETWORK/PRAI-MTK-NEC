// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract Escrow {
    address public owner;
    mapping(address => uint256) public deposits;

    event Deposited(address indexed from, uint256 amount);
    event Released(address indexed to, uint256 amount);

    constructor(){
        owner = msg.sender;
    }

    function deposit() external payable {
        deposits[msg.sender] += msg.value;
        emit Deposited(msg.sender, msg.value);
    }

    function release(address payable to, uint256 amount) external {
        require(msg.sender == owner, "only owner can release");
        require(address(this).balance >= amount, "insufficient escrow balance");
        to.transfer(amount);
        emit Released(to, amount);
    }
}
