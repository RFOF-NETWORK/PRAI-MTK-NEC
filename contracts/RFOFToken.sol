// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "erc20.sol";

contract RFOFToken is ERC20 {
    address public governance;

    constructor(string memory name_, string memory symbol_) ERC20(name_, symbol_) {
        governance = msg.sender;
    }

    modifier onlyGovernance() {
        require(msg.sender == governance, "only governance");
        _;
    }

    function mint(address to, uint256 amount) public onlyGovernance {
        _mint(to, amount);
    }

    function burn(address from, uint256 amount) public onlyGovernance {
        _burn(from, amount);
    }
}
