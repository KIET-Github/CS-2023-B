require('@nomiclabs/hardhat-waffle');

module.exports = {
  solidity: '0.8.0',
  networks: {
    ropsten: {
      url: 'https://eth-goerli.g.alchemy.com/v2/IVr49F1IHCGFMthYawWdatL1RbMdMcTu',
      accounts: ['75985da4e9b674ad1dc0aeaaba8ad0318ad6c0529d9fbcdd6a5eca24a782b477'],
    },
  },
};

