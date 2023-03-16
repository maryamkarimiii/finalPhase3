package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.WalletDTO;
import ir.maktab.finalprojectphase3.data.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    Wallet dtoToModel(WalletDTO walletDTO);

    WalletDTO modelToDto(Wallet wallet);
}
